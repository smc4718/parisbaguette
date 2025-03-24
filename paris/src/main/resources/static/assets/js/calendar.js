document.addEventListener("DOMContentLoaded", function () {
    const calendarEl = document.getElementById("calendar");
    const modal = document.getElementById("reservationModal");
    const closeModal = document.querySelector(".modal .close");
    const displayDate = document.getElementById("displayDate");
    const selectedDateInput = document.getElementById("selectedDate");

    const adminModal = document.getElementById("adminModal");
    const adminContent = document.getElementById("adminContent");
    const openAdminBtn = document.getElementById("openAdminModal");
    const closeAdminModal = document.querySelector("#adminModal .close");

    // adminModal과 openAdminBtn이 존재할 경우만 실행
    if (adminModal && openAdminBtn) {
        openAdminBtn.addEventListener("click", function (event) {
            event.preventDefault();
            adminModal.classList.add("show");
            document.querySelector(".modal-overlay")?.classList.add("show");
            updatePendingReservations();
        });

        if (closeAdminModal) {
            closeAdminModal.addEventListener("click", function () {
                adminModal.classList.remove("show");
                document.querySelector(".modal-overlay")?.classList.remove("show");
            });
        }
    } else {
        console.error("❗️ adminModal 또는 openAdminBtn 요소를 찾을 수 없습니다.");
    }

    const today = new Date();
    let currentYear = today.getFullYear();
    let currentMonth = today.getMonth();

    function fetchReservations(year, month) {
        return fetch(`/reservation/list?year=${year}&month=${month + 1}`)
            .then(response => response.json());
    }

    function renderCalendar(year, month) {
        calendarEl.innerHTML = "";
        const firstDay = new Date(year, month, 1).getDay();
        const lastDate = new Date(year, month + 1, 0).getDate();

        const header = document.createElement("div");
        header.classList.add("calendar-header");
        header.innerHTML = `
            <button id="prevMonth">&lt;</button>
            <span>${year}년 ${month + 1}월</span>
            <button id="nextMonth">&gt;</button>
        `;
        calendarEl.appendChild(header);

        const daysRow = document.createElement("div");
        daysRow.classList.add("calendar-days");
        const days = ["일", "월", "화", "수", "목", "금", "토"];
        days.forEach(day => {
            const dayEl = document.createElement("div");
            dayEl.classList.add("day-label");
            dayEl.textContent = day;
            daysRow.appendChild(dayEl);
        });
        calendarEl.appendChild(daysRow);

        const datesGrid = document.createElement("div");
        datesGrid.classList.add("calendar-grid");

        for (let i = 0; i < firstDay; i++) {
            const emptyCell = document.createElement("div");
            emptyCell.classList.add("empty");
            datesGrid.appendChild(emptyCell);
        }

        fetchReservations(year, month).then(reservations => {
            for (let date = 1; date <= lastDate; date++) {
                const dateCell = document.createElement("div");
                dateCell.classList.add("date");
                dateCell.textContent = date;

                const fullDate = `${year}-${String(month + 1).padStart(2, "0")}-${String(date).padStart(2, "0")}`;
                dateCell.dataset.date = fullDate;

                if (new Date(fullDate) < today) {
                    dateCell.classList.add("disabled");
                } else {
                    dateCell.addEventListener("click", () => openModal(fullDate));
                }

                if (reservations[fullDate]) {
                    reservations[fullDate].forEach(reservation => {
                        const reservationEl = document.createElement("div");
                        reservationEl.classList.add("reservation-item");
                        reservationEl.classList.add(reservation.status.toLowerCase());

                        const userName = reservation.userDto && reservation.userDto.name ? reservation.userDto.name : "";
                        reservationEl.textContent = userName;

                        dateCell.appendChild(reservationEl);
                    });
                }

                datesGrid.appendChild(dateCell);
            }

            calendarEl.appendChild(datesGrid);

            document.getElementById("prevMonth").addEventListener("click", () => {
                currentMonth--;
                if (currentMonth < 0) {
                    currentMonth = 11;
                    currentYear--;
                }
                renderCalendar(currentYear, currentMonth);
            });

            document.getElementById("nextMonth").addEventListener("click", () => {
                currentMonth++;
                if (currentMonth > 11) {
                    currentMonth = 0;
                    currentYear++;
                }
                renderCalendar(currentYear, currentMonth);
            });
        });
    }

    function openModal(date) {
        selectedDateInput.value = date;
        displayDate.textContent = date;
        modal.style.display = "block";
    }

    if (closeModal) {
        closeModal.addEventListener("click", () => {
            modal.style.display = "none";
            location.reload();  // 모달 닫을 경우 페이지 새로고침
        });
    }

    if (adminModal && openAdminBtn) {
        openAdminBtn.addEventListener("click", function (event) {
            event.preventDefault();
            adminModal.classList.add("show"); // 'adminModal' 변수 사용
            document.querySelector(".modal-overlay")?.classList.add("show"); // ?.로 null 체크 추가
            updatePendingReservations(); // 대기 목록 업데이트 추가
        });

        if (closeAdminModal) {
            closeAdminModal.addEventListener("click", function () {
                adminModal.classList.remove("show"); // 'adminModal' 변수 사용
                document.querySelector(".modal-overlay")?.classList.remove("show"); // ?.로 null 체크 추가
            });
        }
    }


    document.addEventListener("click", function (event) {
        if (event.target.classList.contains("approve-btn") || event.target.classList.contains("reject-btn")) {
            event.preventDefault();

            const reservationNo = event.target.getAttribute("data-reservation-no");
            const phoneNumber = event.target.getAttribute("data-phone-number");

            if (!reservationNo) {
                alert("예약 번호가 없습니다. 다시 시도해 주세요.");
                return;
            }

            const action = event.target.classList.contains("approve-btn") ? "approve" : "reject";
            updateReservationStatus(reservationNo, action, phoneNumber, event.target);
        }
    });

    function updateReservationStatus(reservationNo, action, phoneNumber, btnElement) {
        fetch(`/reservation/${action}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: `reservationNo=${reservationNo}&phoneNumber=${encodeURIComponent(phoneNumber)}`
        })
            .then(response => {
                const contentType = response.headers.get("content-type");

                if (contentType && contentType.includes("application/json")) {
                    return response.json();
                } else {
                    return response.text().then(text => ({ message: text }));
                }
            })
            .then(data => {
                alert(data.message || "알 수 없는 응답입니다.");

                // 대기 목록에서 해당 예약 제거
                removePendingReservation(reservationNo);

                // '모든 내역' 즉시 업데이트
                updateAllReservations().then(() => {
                    console.log("모든 내역이 성공적으로 업데이트되었습니다.");
                });
            })
            .catch(error => {
                console.error("에러 발생:", error);
            });
    }

    function removePendingReservation(reservationNo) {
        const pendingRow = document.querySelector(`.approve-btn[data-reservation-no="${reservationNo}"]`)?.closest("tr");
        if (pendingRow) {
            pendingRow.remove();
        }
    }

    function updatePendingReservations() {
        fetch("/reservation/pending")
            .then(response => response.text())
            .then(data => {
                const pendingContainer = document.querySelector("#adminContent");
                if (pendingContainer) {
                    pendingContainer.innerHTML = data;
                }
            })
            .catch(error => console.error("대기 중인 예약 목록 업데이트 오류:", error));
    }

    function formatDate(timestamp) {
        const date = new Date(timestamp);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0'); // 2자리수로 월 표시
        const day = String(date.getDate()).padStart(2, '0');       // 2자리수로 일 표시
        return `${year}-${month}-${day}`;
    }

    function updateAllReservations() {
        fetch("/reservation/all")
            .then(response => {
                if (!response.ok) {
                    throw new Error("서버에서 데이터를 불러오는 데 실패했습니다.");
                }
                return response.json();  // JSON 데이터 처리
            })
            .then(data => {
                const allReservationsContainer = document.getElementById("allReservationsContainer");

                if (allReservationsContainer) {
                    let html = `
                    <table>
                        <thead>
                            <tr>
                                <th>예약 번호</th>
                                <th>사용자 이름</th>
                                <th>예약 날짜</th>
                                <th>상태</th>
                            </tr>
                        </thead>
                        <tbody>
                `;

                    data.forEach(reservation => {
                        html += `
                        <tr>
                            <td>${reservation.reservationNo}</td>
                            <td>${reservation.userDto?.name || '-'}</td>
                            <td>${formatDate(reservation.reservationDate)}</td> <!-- 수정된 부분 -->
                            <td class="status-column">${reservation.statusLabel}</td>
                        </tr>
                    `;
                    });

                    html += `</tbody></table>`;
                    allReservationsContainer.innerHTML = html;
                } else {
                    console.error("❗️ '모든 내역'을 찾을 수 없습니다. HTML 구조를 확인하세요.");
                }
            })
            .catch(error => console.error("모든 내역 업데이트 오류:", error));
    }

    renderCalendar(currentYear, currentMonth);
});
