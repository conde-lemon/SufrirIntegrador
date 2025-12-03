// Header dropdown - Función global disponible inmediatamente (SIN IIFE)
function toggleDropdown(event) {
    if (event) {
        event.stopPropagation();
        event.preventDefault();
    }

    const dropdown = document.getElementById('userDropdown');
    if (!dropdown) return;

    const isActive = dropdown.classList.toggle('active');
    const btn = dropdown.querySelector('.dropdown-toggle');
    if (btn) btn.setAttribute('aria-expanded', isActive ? 'true' : 'false');
}

function closeDropdown() {
    const dropdown = document.getElementById('userDropdown');
    if (!dropdown) return;

    dropdown.classList.remove('active');
    const btn = dropdown.querySelector('.dropdown-toggle');
    if (btn) btn.setAttribute('aria-expanded', 'false');
}

// Event listeners después de que el DOM carga
document.addEventListener('DOMContentLoaded', function () {
    console.log('✅ header.js loaded - toggleDropdown is available globally');

    // Cerrar al hacer click fuera
    document.addEventListener('click', function (event) {
        const dropdown = document.getElementById('userDropdown');
        if (dropdown && !dropdown.contains(event.target)) {
            closeDropdown();
        }
    });

    // Cerrar con tecla Escape
    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape') {
            closeDropdown();
        }
    });

    // Soporte de teclado en el botón toggle
    const userToggle = document.getElementById('userDropdownToggle');
    if (userToggle) {
        userToggle.addEventListener('keydown', function (e) {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                toggleDropdown(e);
            }
        });
    }
});


