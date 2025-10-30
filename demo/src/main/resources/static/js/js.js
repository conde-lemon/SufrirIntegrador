document.addEventListener('DOMContentLoaded', () => {
    // Menú hamburguesa
    const btnNavbar = document.getElementById("btn-navbar");
    if (btnNavbar) {
        btnNavbar.addEventListener("click", () => {
            const menu = document.getElementById("id_ul_navbar");
            if (menu) {
                menu.classList.toggle("active");
            }
        });
    }

    // Elementos del formulario de búsqueda
    const selectFrom = document.getElementById('select-from');
    const selectTo = document.getElementById('select-to');
    const searchBtn = document.getElementById('search-service-btn');
    const serviceTypeInput = document.getElementById('service-type');
    const searchTitle = document.getElementById('search-title');
    const categoryItems = document.querySelectorAll('.category-item');

    if (!selectFrom || !selectTo || !searchBtn) {
        return;
    }

    // Configuración de tipos de servicio
    const serviceConfig = {
        'VUELO': { title: 'Busca tu próximo vuelo', button: 'Buscar Vuelos' },
        'CRUCERO': { title: 'Busca tu próximo crucero', button: 'Buscar Cruceros' },
        'BUS': { title: 'Busca tu próximo bus', button: 'Buscar Buses' }
    };

    // Cambiar tipo de servicio
    categoryItems.forEach(item => {
        item.addEventListener('click', (e) => {
            e.preventDefault();
            
            // Actualizar clases activas
            categoryItems.forEach(i => i.classList.remove('active'));
            item.classList.add('active');
            
            // Obtener tipo de servicio
            const tipo = item.dataset.tipo;
            const config = serviceConfig[tipo];
            
            // Actualizar formulario
            serviceTypeInput.value = tipo;
            searchTitle.textContent = config.title;
            searchBtn.textContent = config.button;
        });
    });

    // Validación del formulario
    const checkSelectors = () => {
        const fromValue = selectFrom.value;
        const toValue = selectTo.value;

        if (fromValue && toValue && fromValue !== toValue) {
            searchBtn.classList.remove('hidden');
        } else {
            searchBtn.classList.add('hidden');
        }
    };

    selectFrom.addEventListener('change', checkSelectors);
    selectTo.addEventListener('change', checkSelectors);
});