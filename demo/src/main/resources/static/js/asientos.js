document.addEventListener('DOMContentLoaded', function() {
    const seats = document.querySelectorAll('.seat.available');
    const selectedSeatInput = document.getElementById('asientoSeleccionado');
    const selectedSeatDisplay = document.getElementById('selected-seat-display');
    let selectedSeat = null;

    // Marcar algunos asientos como ocupados aleatoriamente
    seats.forEach((seat, index) => {
        if (Math.random() < 0.3) { // 30% de probabilidad de estar ocupado
            seat.classList.remove('available');
            seat.classList.add('occupied');
            seat.disabled = true;
        }
    });

    // Manejar selección de asientos
    seats.forEach(seat => {
        seat.addEventListener('click', function() {
            if (this.classList.contains('occupied')) return;

            // Remover selección anterior
            if (selectedSeat) {
                selectedSeat.classList.remove('selected');
                selectedSeat.classList.add('available');
            }

            // Seleccionar nuevo asiento
            this.classList.remove('available');
            this.classList.add('selected');
            selectedSeat = this;

            // Actualizar campos
            const seatNumber = this.dataset.seat;
            selectedSeatInput.value = seatNumber;
            selectedSeatDisplay.textContent = seatNumber;
        });
    });

    // Validación del formulario
    const form = document.querySelector('form');
    form.addEventListener('submit', function(e) {
        if (!selectedSeatInput.value) {
            e.preventDefault();
            alert('Por favor selecciona un asiento antes de continuar.');
        }
    });
});