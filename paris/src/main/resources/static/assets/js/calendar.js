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

    closeModal.addEventListener("click", () => {
        modal.style.display = "none";
    });

    window.addEventListener("click", (event) => {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    });

    openAdminBtn.addEventListener("click", function (event) {
        event.preventDefault();
        refreshPendingList();
    });

    closeAdminModal.addEventListener("click", function () {
        adminModal.style.display = "none";
        document.getElementById("adminModal").classList.remove("show");
        document.querySelector(".modal-overlay").classList.remove("show");
    });

    window.addEventListener("click", function (event) {
        if (event.target === adminModal) {
            adminModal.style.display = "none";
            document.getElementById("adminModal").classList.remove("show");
            document.querySelector(".modal-overlay").classList.remove("show");
        }
    });

    /** 승인 및 거절 버튼 클릭 이벤트 핸들러 */
    document.addEventListener("click", function (event) {
        if (event.target.classList.contains("approve-btn") || event.target.classList.contains("reject-btn")) {
            event.preventDefault();

            const reservationNo = event.target.getAttribute("data-reservation-no");
            const phoneNumber = event.target.getAttribute("data-phone-number");  // 전화번호 가져오기
            console.log("Clicked reservationNo:", reservationNo);  // 값 확인
            console.log("Phone Number:", phoneNumber);  // 값 확인

            if (!reservationNo) {
                alert("예약 번호가 없습니다. 다시 시도해 주세요.");
                return;
            }

            const action = event.target.classList.contains("approve-btn") ? "approve" : "reject";
            updateReservationStatus(reservationNo, action, phoneNumber);  // 전화번호와 함께 전달
        }
    });


    function updateReservationStatus(reservationNo, action, phoneNumber) {
        console.log('reservationNo:', reservationNo);  // reservationNo 값 출력 확인
        console.log('phoneNumber:', phoneNumber);  // phoneNumber 값 출력 확인

        const intReservationNo = parseInt(reservationNo, 10);  // reservationNo를 숫자로 변환

        // reservationNo가 유효한 숫자인지 확인
        if (isNaN(intReservationNo)) {
            alert("예약 번호가 잘못되었습니다.");
            return;
        }

        fetch(`/reservation/${action}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: `reservationNo=${intReservationNo}&phoneNumber=${encodeURIComponent(phoneNumber)}`  // 전화번호와 함께 전달
        })
            .then(response => response.json()) // JSON으로 응답 받기
            .then(data => {
                alert(data.message);  // 응답 메시지 출력 ("예약이 승인되었습니다." 등)
                refreshPendingList(); // 모달 내부 목록 업데이트
            })
            .catch(error => {
                console.error("에러 발생:", error);
            });
    }


    function refreshPendingList() {
        fetch("/reservation/pending")  // 최신 목록 다시 불러오기
            .then(response => response.text())
            .then(data => {
                adminContent.innerHTML = data;
                adminModal.style.display = "block";
                document.getElementById("adminModal").classList.add("show");
                document.querySelector(".modal-overlay").classList.add("show");
            })
            .catch(error => console.error("목록 업데이트 오류:", error));
    }

    renderCalendar(currentYear, currentMonth);
});
