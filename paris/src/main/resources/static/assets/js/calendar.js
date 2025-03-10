document.addEventListener("DOMContentLoaded", function () {
    const calendarEl = document.getElementById("calendar");
    const modal = document.getElementById("reservationModal");
    const closeModal = document.querySelector(".modal .close");
    const displayDate = document.getElementById("displayDate");
    const selectedDateInput = document.getElementById("selectedDate");

    // 현재 날짜 정보
    const today = new Date();
    let currentYear = today.getFullYear();
    let currentMonth = today.getMonth();

    function renderCalendar(year, month) {
        calendarEl.innerHTML = ""; // 기존 달력 초기화
        const firstDay = new Date(year, month, 1).getDay(); // 월 시작 요일
        const lastDate = new Date(year, month + 1, 0).getDate(); // 해당 월의 마지막 날짜

        // 달력 헤더 (연도 및 월)
        const header = document.createElement("div");
        header.classList.add("calendar-header");
        header.innerHTML = `
            <button id="prevMonth">&lt;</button>
            <span>${year}년 ${month + 1}월</span>
            <button id="nextMonth">&gt;</button>
        `;
        calendarEl.appendChild(header);

        // 요일 표시
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

        // 날짜 표시
        const datesGrid = document.createElement("div");
        datesGrid.classList.add("calendar-grid");

        // 이전 달의 빈 칸
        for (let i = 0; i < firstDay; i++) {
            const emptyCell = document.createElement("div");
            emptyCell.classList.add("empty");
            datesGrid.appendChild(emptyCell);
        }

        // 날짜 채우기
        for (let date = 1; date <= lastDate; date++) {
            const dateCell = document.createElement("div");
            dateCell.classList.add("date");
            dateCell.textContent = date;

            const fullDate = `${year}-${String(month + 1).padStart(2, "0")}-${String(date).padStart(2, "0")}`;
            dateCell.dataset.date = fullDate;

            // 오늘 이전의 날짜는 비활성화
            if (new Date(fullDate) < today) {
                dateCell.classList.add("disabled");
            } else {
                dateCell.addEventListener("click", () => openModal(fullDate));
            }

            datesGrid.appendChild(dateCell);
        }

        calendarEl.appendChild(datesGrid);

        // 이전/다음 월 버튼 이벤트
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

    // 초기 달력 렌더링
    renderCalendar(currentYear, currentMonth);
});
