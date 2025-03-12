document.addEventListener("DOMContentLoaded", function () {
    const calendarEl = document.getElementById("calendar");
    const modal = document.getElementById("reservationModal");
    const closeModal = document.querySelector(".modal .close");
    const displayDate = document.getElementById("displayDate");
    const selectedDateInput = document.getElementById("selectedDate");

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

                // 예약이 있을 경우 사용자 이름을 표시
                if (reservations[fullDate]) {
                    reservations[fullDate].forEach(reservation => {
                        const reservationEl = document.createElement("div");
                        reservationEl.classList.add("reservation-item");
                        reservationEl.classList.add(reservation.status.toLowerCase());

                        // 예약자 이름을 userDto가 있을 경우 표시
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

    renderCalendar(currentYear, currentMonth);
});
