export function formatDate(value) {
  if (!value) return '—';
  const date = new Date(`${String(value).slice(0, 10)}T00:00:00`);
  return Number.isNaN(date.getTime()) ? value : date.toLocaleDateString('en-IN', {
    day: '2-digit', month: 'short', year: 'numeric',
  });
}

export function formatCurrency(value) {
  if (value === null || value === undefined || value === '') return '—';
  return new Intl.NumberFormat('en-IN', {
    style: 'currency', currency: 'INR', maximumFractionDigits: 0,
  }).format(Number(value));
}

export function initials(value = '') {
  return String(value).trim().split(/\s+/).filter(Boolean).slice(0, 2).map((part) => part[0]?.toUpperCase()).join('') || 'HR';
}

export function employeeName(employee = {}) {
  return [employee.firstName, employee.lastName].filter(Boolean).join(' ') || 'Unnamed employee';
}

export function getPageData(response) {
  const data = response?.data;
  if (Array.isArray(data)) return { content: data, totalElements: data.length, totalPages: 1, number: 0, size: data.length };
  return {
    content: Array.isArray(data?.content) ? data.content : [],
    totalElements: Number(data?.totalElements ?? data?.content?.length ?? 0),
    totalPages: Number(data?.totalPages ?? 1),
    number: Number(data?.number ?? 0),
    size: Number(data?.size ?? 10),
  };
}
