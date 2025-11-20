document.addEventListener('DOMContentLoaded', function() {
    const seats = document.querySelectorAll('.seat.available');
    const selectedSeatInput = document.getElementById('asientoSeleccionado');
    const selectedSeatDisplay = document.getElementById('selected-seat-display');
    const equipajeTotalDisplay = document.getElementById('equipaje-total');
    const totalPriceDisplay = document.getElementById('total-price');
    const baggageCheckboxes = document.querySelectorAll('input[name="equipajeIds"]');
    
    let selectedSeat = null;
    let basePrice = 0;
    
    // Obtener precio base del servicio desde el elemento correcto
    const basePriceElements = document.querySelectorAll('.summary-item');
    basePriceElements.forEach(item => {
        const label = item.querySelector('span:first-child');
        if (label && label.textContent.includes('Precio Base')) {
            const priceElement = item.querySelector('span:last-child');
            if (priceElement) {
                const priceText = priceElement.textContent.replace('S/ ', '').replace(',', '');
                basePrice = parseFloat(priceText) || 0;
            }
        }
    });
    
    if (basePrice > 0) {
        console.log('Base price loaded:', basePrice);
    } else {
        console.warn('Base price not found or invalid');
    }

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
            
            console.log('Seat selected:', seatNumber);
        });
    });

    // Calcular total de equipaje
    function calculateBaggageTotal() {
        let baggageTotal = 0;
        
        baggageCheckboxes.forEach(checkbox => {
            if (checkbox.checked) {
                const priceElement = checkbox.parentElement.querySelector('.baggage-price');
                if (priceElement) {
                    const priceText = priceElement.textContent.replace('+ S/ ', '').replace(',', '');
                    baggageTotal += parseFloat(priceText) || 0;
                }
            }
        });
        
        return baggageTotal;
    }

    // Actualizar totales
    function updateTotals() {
        const baggageTotal = calculateBaggageTotal();
        const totalPrice = basePrice + baggageTotal;
        
        equipajeTotalDisplay.textContent = `S/ ${baggageTotal.toFixed(2)}`;
        totalPriceDisplay.textContent = `S/ ${totalPrice.toFixed(2)}`;
    }

    // Escuchar cambios en checkboxes de equipaje
    baggageCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', updateTotals);
    });

    // Validación del formulario
    const form = document.querySelector('form');
    form.addEventListener('submit', function(e) {
        if (!selectedSeatInput.value || selectedSeatInput.value.trim() === '') {
            e.preventDefault();
            alert('Por favor selecciona un asiento antes de continuar.');
            return;
        }
        
        // Validar campos requeridos
        const requiredFields = form.querySelectorAll('input[required], select[required]');
        let hasErrors = false;
        let emptyFields = [];
        
        requiredFields.forEach(field => {
            if (!field.value.trim()) {
                field.classList.add('error');
                hasErrors = true;
                emptyFields.push(field.getAttribute('title') || field.name || 'campo');
            } else {
                field.classList.remove('error');
            }
        });
        
        if (hasErrors) {
            e.preventDefault();
            alert('Por favor completa todos los campos requeridos.');
        }
    });
    
    // Inicializar totales
    updateTotals();
});