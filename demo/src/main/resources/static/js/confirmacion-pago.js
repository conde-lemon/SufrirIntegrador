document.addEventListener('DOMContentLoaded', function() {
    // Animación de entrada
    const container = document.querySelector('.confirmacion-container');
    if (container) {
        container.style.opacity = '0';
        container.style.transform = 'translateY(30px)';
        
        setTimeout(() => {
            container.style.transition = 'all 0.6s ease-out';
            container.style.opacity = '1';
            container.style.transform = 'translateY(0)';
        }, 100);
    }

    // Copiar referencia al clipboard
    const referenciaElement = document.querySelector('.referencia-codigo');
    if (referenciaElement) {
        referenciaElement.addEventListener('click', function() {
            const referencia = this.textContent;
            navigator.clipboard.writeText(referencia).then(() => {
                // Mostrar feedback visual
                const originalText = this.textContent;
                this.textContent = '¡Copiado!';
                this.style.backgroundColor = '#4caf50';
                
                setTimeout(() => {
                    this.textContent = originalText;
                    this.style.backgroundColor = '';
                }, 2000);
            });
        });
        
        // Agregar cursor pointer
        referenciaElement.style.cursor = 'pointer';
        referenciaElement.title = 'Click para copiar';
    }

    // Animación de botones
    const buttons = document.querySelectorAll('.btn');
    buttons.forEach(button => {
        button.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-2px)';
        });
        
        button.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
        });
    });

    // Auto-redirect después de 30 segundos (opcional)
    setTimeout(() => {
        const autoRedirect = confirm('¿Deseas ir a "Mis Reservas" ahora?');
        if (autoRedirect) {
            window.location.href = '/reservas';
        }
    }, 30000);
});