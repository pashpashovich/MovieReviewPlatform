function applyFilters() {
  const form = document.getElementById('filterForm');
  const formData = new FormData(form);
  const queryString = new URLSearchParams(formData).toString();
  console.log(" "+queryString)
  window.location.href = `${window.location.pathname}?${queryString}`;
}

function resetFilters() {
  const form = document.getElementById('filterForm');
  form.reset();
  window.location.href = window.location.pathname;
}
