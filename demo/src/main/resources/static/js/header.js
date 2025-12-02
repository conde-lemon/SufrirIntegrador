// Global dropdown handling for header (toggle on click, close on outside click / Escape)
(function () {
    function toggleDropdown(event) {
        event && event.stopPropagation();
        event && event.preventDefault();
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

    document.addEventListener('DOMContentLoaded', function () {
        // Expose toggleDropdown globally so inline onclick handlers work
        window.toggleDropdown = toggleDropdown;
        window.closeDropdown = closeDropdown;

        // Close when clicking outside
        document.addEventListener('click', function (event) {
            const dropdown = document.getElementById('userDropdown');
            if (dropdown && !dropdown.contains(event.target)) {
                closeDropdown();
            }
        });

        // Close with Escape
        document.addEventListener('keydown', function (e) {
            if (e.key === 'Escape') closeDropdown();
        });

        // Keyboard support on the toggle button
        const userToggle = document.getElementById('userDropdownToggle');
        if (userToggle) {
            userToggle.addEventListener('keydown', function (e) {
                if (e.key === 'Enter' || e.key === ' ') {
                    e.preventDefault();
                    toggleDropdown(e);
                }
            });
        }

        // Prevent hover from showing menu (safety)
        document.querySelectorAll('.dropdown').forEach(function (el) {
            el.addEventListener('mouseenter', function () {
                // do nothing - keep click behavior
            });
        });
    });
})();

