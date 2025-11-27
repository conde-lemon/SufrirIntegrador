function openBoletaModal(reservaId) {
    const modal = document.getElementById('boletaModal');
    const frame = document.getElementById('boletaFrame');
    if (modal && frame) {
        frame.src = '/boleta/reserva/' + reservaId + '/preview';
        modal.style.display = 'block';
        document.body.style.overflow = 'hidden';
    }
}

function closeBoletaModal() {
    const modal = document.getElementById('boletaModal');
    if (modal) {
        modal.style.display = 'none';
        document.body.style.overflow = 'auto';
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const modal = document.getElementById('boletaModal');
    if (modal) {
        modal.addEventListener('click', function(e) {
            if (e.target === this) {
                closeBoletaModal();
            }
        });
    }
});
