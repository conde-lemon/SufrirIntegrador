document.addEventListener('DOMContentLoaded', function() {
    const paymentMethods = document.querySelectorAll('input[name="metodoPago"]');
    const cardForm = document.getElementById('card-form');
    const cardNumber = document.getElementById('card-number');
    const expiryDate = document.getElementById('expiry-date');
    const cvv = document.getElementById('cvv');

    // Manejar cambio de método de pago
    paymentMethods.forEach(method => {
        method.addEventListener('change', function() {
            if (this.value === 'tarjeta') {
                cardForm.style.display = 'block';
            } else {
                cardForm.style.display = 'none';
            }
        });
    });

    // Formatear número de tarjeta
    cardNumber.addEventListener('input', function() {
        let value = this.value.replace(/\s/g, '').replace(/[^0-9]/gi, '');
        let formattedValue = value.match(/.{1,4}/g)?.join(' ') || value;
        this.value = formattedValue;
    });

    // Formatear fecha de vencimiento
    expiryDate.addEventListener('input', function() {
        let value = this.value.replace(/\D/g, '');
        if (value.length >= 2) {
            value = value.substring(0, 2) + '/' + value.substring(2, 4);
        }
        this.value = value;
    });

    // Solo números en CVV
    cvv.addEventListener('input', function() {
        this.value = this.value.replace(/[^0-9]/g, '');
    });

    // Validación del formulario
    const form = document.querySelector('form');
    form.addEventListener('submit', function(e) {
        const selectedMethod = document.querySelector('input[name="metodoPago"]:checked').value;
        
        if (selectedMethod === 'tarjeta') {
            if (!cardNumber.value || !expiryDate.value || !cvv.value) {
                e.preventDefault();
                alert('Por favor completa todos los campos de la tarjeta.');
                return;
            }
        }

        // Mostrar loading
        const button = document.querySelector('.pay-button');
        button.innerHTML = '<span class="button-icon">⏳</span> Procesando...';
        button.disabled = true;
    });
});