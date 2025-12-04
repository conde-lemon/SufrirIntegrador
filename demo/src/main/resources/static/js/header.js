/**
 * Travel4U - Header JavaScript
 * Maneja toda la funcionalidad del header principal
 */

// Función principal del header que se ejecuta cuando el DOM está listo
function initializeHeader() {
    // Toggle menú hamburguesa
    const btnNavbar = document.getElementById('btn-navbar');
    const mainNav = document.getElementById('id_ul_navbar');

    if (btnNavbar && mainNav) {
        // Remover listeners anteriores si existen (evitar duplicados)
        const newBtnNavbar = btnNavbar.cloneNode(true);
        btnNavbar.parentNode.replaceChild(newBtnNavbar, btnNavbar);

        newBtnNavbar.addEventListener('click', (e) => {
            e.stopPropagation();
            newBtnNavbar.classList.toggle('active');
            mainNav.classList.toggle('active');

            // Prevenir scroll en móvil
            if (window.innerWidth <= 768) {
                document.body.style.overflow = mainNav.classList.contains('active') ? 'hidden' : 'auto';
            }
        });

        // Cerrar menú al hacer clic en un enlace (móvil)
        const navLinks = mainNav.querySelectorAll('.nav-link');
        navLinks.forEach(link => {
            link.addEventListener('click', () => {
                if (window.innerWidth <= 768) {
                    newBtnNavbar.classList.remove('active');
                    mainNav.classList.remove('active');
                    document.body.style.overflow = 'auto';
                }
            });
        });
    }

    // Toggle dropdown de usuario
    const userDropdownBtn = document.getElementById('user-dropdown-btn');
    if (userDropdownBtn) {
        const userDropdown = userDropdownBtn.closest('.user-dropdown');

        if (userDropdown) {
            // Remover listener anterior si existe
            const newUserBtn = userDropdownBtn.cloneNode(true);
            userDropdownBtn.parentNode.replaceChild(newUserBtn, userDropdownBtn);

            // Agregar nuevo listener
            newUserBtn.addEventListener('click', (e) => {
                e.preventDefault();
                e.stopPropagation();
                console.log('Toggle dropdown clicked'); // Debug
                userDropdown.classList.toggle('active');
            });

            // Cerrar dropdown al hacer clic fuera
            document.addEventListener('click', (e) => {
                if (userDropdown && !userDropdown.contains(e.target)) {
                    userDropdown.classList.remove('active');
                }
            });

            // Cerrar dropdown al presionar ESC
            document.addEventListener('keydown', (e) => {
                if (e.key === 'Escape' && userDropdown.classList.contains('active')) {
                    userDropdown.classList.remove('active');
                }
            });
        }
    }

    // Marcar enlace activo según la URL
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.nav-link');

    navLinks.forEach(link => {
        const href = link.getAttribute('href');

        // Remover clase active de todos
        link.classList.remove('active');

        // Agregar clase active al enlace correspondiente
        if (href === currentPath || (currentPath === '/' && href === '/')) {
            link.classList.add('active');
        }
    });

    // Integración con el formulario de búsqueda si existe
    const categoryItems = document.querySelectorAll('.nav-link[data-tipo]');
    const serviceTypeInput = document.getElementById('service-type');
    const searchTitle = document.getElementById('search-title');
    const searchBtn = document.getElementById('search-service-btn');

    if (categoryItems.length > 0 && serviceTypeInput) {
        const serviceConfig = {
            'VUELO': { title: 'Busca tu próximo vuelo', button: 'Buscar Vuelos' },
            'CRUCERO': { title: 'Busca tu próximo crucero', button: 'Buscar Cruceros' },
            'BUS': { title: 'Busca tu próximo bus', button: 'Buscar Buses' }
        };

        categoryItems.forEach(item => {
            item.addEventListener('click', (e) => {
                e.preventDefault();

                // Scroll suave a la sección de búsqueda
                const searchSection = document.querySelector('.search-section');
                if (searchSection) {
                    searchSection.scrollIntoView({ behavior: 'smooth', block: 'start' });
                }

                // Actualizar tipo de servicio
                const tipo = item.dataset.tipo;
                const config = serviceConfig[tipo];

                if (config) {
                    serviceTypeInput.value = tipo;
                    if (searchTitle) searchTitle.textContent = config.title;
                    if (searchBtn) searchBtn.textContent = config.button;

                    // Actualizar clases activas en los items de categoría
                    document.querySelectorAll('.category-item').forEach(cat => {
                        cat.classList.remove('active');
                        if (cat.dataset.tipo === tipo) {
                            cat.classList.add('active');
                        }
                    });
                }
            });
        });
    }

    // Restaurar scroll al cambiar tamaño de ventana
    window.addEventListener('resize', () => {
        if (window.innerWidth > 768) {
            document.body.style.overflow = 'auto';
            if (mainNav) mainNav.classList.remove('active');
            const hamburger = document.getElementById('btn-navbar');
            if (hamburger) hamburger.classList.remove('active');
        }
    });

    console.log('Header initialized successfully'); // Debug
}

// Ejecutar cuando el DOM esté completamente cargado
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initializeHeader);
} else {
    // DOM ya está listo, ejecutar inmediatamente
    initializeHeader();
}

// También ejecutar después de que Thymeleaf haya renderizado (por si acaso)
window.addEventListener('load', () => {
    // Verificar si el header ya fue inicializado
    const userDropdownBtn = document.getElementById('user-dropdown-btn');
    if (userDropdownBtn && !userDropdownBtn.hasAttribute('data-initialized')) {
        userDropdownBtn.setAttribute('data-initialized', 'true');
        initializeHeader();
    }
});

