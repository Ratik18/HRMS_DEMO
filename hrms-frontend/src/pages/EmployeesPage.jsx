import { useEffect, useMemo, useState } from 'react';
import { ArrowDown, ArrowUp, ChevronLeft, ChevronRight, Edit3, Plus, Search, Trash2, Users, X } from 'lucide-react';
import Modal from '../components/Modal';
import { Alert, EmptyState, Loading } from '../components/Feedback';
import { departmentService, designationService, employeeService } from '../services/employeeService';
import { getApiError } from '../services/api';
import { employeeName, formatCurrency, formatDate, getPageData, initials } from '../utils/format';

const emptyForm = { firstName: '', lastName: '', email: '', phone: '', dateOfJoining: '', salary: '', status: 'ACTIVE', departmentId: '', designationId: '' };

export default function EmployeesPage() {
  const [employees, setEmployees] = useState([]);
  const [departments, setDepartments] = useState([]);
  const [designations, setDesignations] = useState([]);
  const [page, setPage] = useState(0);
  const [pageInfo, setPageInfo] = useState({ totalElements: 0, totalPages: 1, size: 10 });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [notice, setNotice] = useState('');
  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState(emptyForm);
  const [saving, setSaving] = useState(false);
  const [deleteTarget, setDeleteTarget] = useState(null);

  const load = async (targetPage = page) => {
    setLoading(true); setError('');
    try {
      const [employeeResponse, departmentResponse, designationResponse] = await Promise.all([
        employeeService.list({ page: targetPage, size: 10 }),
        departmentService.list(), designationService.list(),
      ]);
      const pageData = getPageData(employeeResponse);
      setEmployees(pageData.content); setPageInfo(pageData); setDepartments(departmentResponse.data || []); setDesignations(designationResponse.data || []);
    } catch (err) { setError(getApiError(err)); }
    finally { setLoading(false); }
  };

  useEffect(() => { load(page); }, [page]);
  const filtered = useMemo(() => employees.filter((employee) => {
    const haystack = `${employeeName(employee)} ${employee.email || ''} ${employee.phone || ''} ${employee.department?.name || employee.departmentName || ''} ${employee.designation?.title || employee.designationTitle || ''}`.toLowerCase();
    return haystack.includes(search.toLowerCase()) && (statusFilter === 'ALL' || employee.status === statusFilter);
  }), [employees, search, statusFilter]);

  const openCreate = () => { setEditing(null); setForm(emptyForm); setError(''); setNotice(''); setModalOpen(true); };
  const openEdit = (employee) => {
    setEditing(employee);
    setForm({ firstName: employee.firstName || '', lastName: employee.lastName || '', email: employee.email || '', phone: employee.phone || '', dateOfJoining: employee.dateOfJoining || '', salary: employee.salary ?? '', status: employee.status || 'ACTIVE', departmentId: employee.department?.id ?? employee.departmentId ?? '', designationId: employee.designation?.id ?? employee.designationId ?? '' });
    setError(''); setNotice(''); setModalOpen(true);
  };
  const save = async (event) => {
    event.preventDefault(); setSaving(true); setError('');
    const payload = {
      firstName: form.firstName.trim(), lastName: form.lastName.trim(), email: form.email.trim(),
      phone: form.phone.trim() || null, dateOfJoining: form.dateOfJoining,
      salary: form.salary === '' ? null : Number(form.salary), status: form.status,
      department: { id: Number(form.departmentId) }, designation: { id: Number(form.designationId) },
    };
    try {
      if (editing) await employeeService.update(editing.id, payload); else await employeeService.create(payload);
      setModalOpen(false); setNotice(editing ? 'Employee record updated.' : 'Employee created successfully.'); await load(page);
    } catch (err) { setError(getApiError(err)); }
    finally { setSaving(false); }
  };
  const remove = async () => {
    if (!deleteTarget) return;
    setSaving(true); setError('');
    try { await employeeService.remove(deleteTarget.id); setDeleteTarget(null); setNotice('Employee deleted.'); await load(page); }
    catch (err) { setError(getApiError(err)); }
    finally { setSaving(false); }
  };

  return <div className="stack-lg">
    <div className="toolbar-row"><div className="toolbar-copy"><div className="eyebrow">PEOPLE DIRECTORY</div><h2>Employee records <span className="count-chip">{pageInfo.totalElements}</span></h2><p>Keep employee information accurate and easy to find.</p></div><button className="button button-primary" onClick={openCreate}><Plus size={17}/> Add employee</button></div>
    {notice && <Alert type="success">{notice}</Alert>}{error && <Alert>{error}</Alert>}
    <section className="panel table-panel">
      <div className="table-toolbar"><div className="search-field"><Search size={17}/><input placeholder="Search name, email or team..." value={search} onChange={(e) => setSearch(e.target.value)}/><kbd>⌘ K</kbd></div><select className="select-control filter-select" value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)}><option value="ALL">All statuses</option><option value="ACTIVE">Active</option><option value="INACTIVE">Inactive</option><option value="ON_LEAVE">On leave</option><option value="TERMINATED">Terminated</option></select></div>
      {loading ? <Loading label="Loading employees..."/> : filtered.length ? <>
        <div className="table-scroll"><table className="data-table"><thead><tr><th>EMPLOYEE</th><th>DEPARTMENT</th><th>DESIGNATION</th><th>JOINING DATE</th><th>STATUS</th><th className="align-right">SALARY</th><th></th></tr></thead><tbody>{filtered.map((employee) => <tr key={employee.id}><td><div className="employee-cell"><div className="avatar avatar-table">{initials(employeeName(employee))}</div><div><strong>{employeeName(employee)}</strong><small>{employee.email}</small></div></div></td><td>{employee.department?.name || employee.departmentName || '—'}</td><td>{employee.designation?.title || employee.designationTitle || '—'}</td><td>{formatDate(employee.dateOfJoining)}</td><td><span className={`status-badge status-${String(employee.status || 'unknown').toLowerCase()}`}>{(employee.status || 'Unknown').replace('_', ' ')}</span></td><td className="align-right salary-cell">{formatCurrency(employee.salary)}</td><td><div className="row-actions"><button className="icon-button" title="Edit employee" onClick={() => openEdit(employee)}><Edit3 size={16}/></button><button className="icon-button danger-icon" title="Delete employee" onClick={() => setDeleteTarget(employee)}><Trash2 size={16}/></button></div></td></tr>)}</tbody></table></div>
        <div className="table-footer"><span>Showing {filtered.length} of {pageInfo.totalElements} records on this page</span><div className="pagination"><button className="icon-button" disabled={page <= 0} onClick={() => setPage((v) => Math.max(0, v - 1))}><ChevronLeft size={17}/></button><span>Page {page + 1} of {Math.max(1, pageInfo.totalPages)}</span><button className="icon-button" disabled={page + 1 >= pageInfo.totalPages} onClick={() => setPage((v) => v + 1)}><ChevronRight size={17}/></button></div></div>
      </> : <EmptyState title={search || statusFilter !== 'ALL' ? 'No matching employees' : 'Your employee directory is empty'} description={search || statusFilter !== 'ALL' ? 'Try another search or status filter.' : 'Add your first employee to start building your people directory.'} action={!search && statusFilter === 'ALL' && <button className="button button-primary" onClick={openCreate}><Plus size={16}/> Add employee</button>}/>}
    </section>

    {modalOpen && <Modal title={editing ? 'Edit employee' : 'Add employee'} subtitle="Employee fields follow the Employee Service model in your project." onClose={() => setModalOpen(false)} size="wide"><form className="form-grid" onSubmit={save}>
      <FormField label="First name"><input required value={form.firstName} onChange={(e) => setForm({ ...form, firstName: e.target.value })}/></FormField>
      <FormField label="Last name"><input required value={form.lastName} onChange={(e) => setForm({ ...form, lastName: e.target.value })}/></FormField>
      <FormField label="Email address"><input required type="email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })}/></FormField>
      <FormField label="Phone number"><input value={form.phone} onChange={(e) => setForm({ ...form, phone: e.target.value })}/></FormField>
      <FormField label="Date of joining"><input required type="date" value={form.dateOfJoining} onChange={(e) => setForm({ ...form, dateOfJoining: e.target.value })}/></FormField>
      <FormField label="Salary (INR)"><input type="number" min="0" step="0.01" value={form.salary} onChange={(e) => setForm({ ...form, salary: e.target.value })}/></FormField>
      <FormField label="Department"><select required value={form.departmentId} onChange={(e) => setForm({ ...form, departmentId: e.target.value })}><option value="">Select department</option>{departments.map((item) => <option key={item.id} value={item.id}>{item.name}</option>)}</select></FormField>
      <FormField label="Designation"><select required value={form.designationId} onChange={(e) => setForm({ ...form, designationId: e.target.value })}><option value="">Select designation</option>{designations.map((item) => <option key={item.id} value={item.id}>{item.title}</option>)}</select></FormField>
      <FormField label="Employment status"><select value={form.status} onChange={(e) => setForm({ ...form, status: e.target.value })}><option value="ACTIVE">Active</option><option value="INACTIVE">Inactive</option><option value="ON_LEAVE">On leave</option><option value="TERMINATED">Terminated</option></select></FormField>
      {error && <div className="form-full"><Alert>{error}</Alert></div>}
      <div className="form-actions form-full"><button className="button button-quiet" type="button" onClick={() => setModalOpen(false)}>Cancel</button><button className="button button-primary" disabled={saving}>{saving ? 'Saving...' : editing ? 'Save changes' : 'Create employee'}</button></div>
    </form></Modal>}
    {deleteTarget && <Modal title="Delete employee?" subtitle={`This will remove ${employeeName(deleteTarget)} from the employee directory.`} onClose={() => setDeleteTarget(null)}><div className="confirm-actions"><button className="button button-quiet" onClick={() => setDeleteTarget(null)}>Cancel</button><button className="button button-danger" disabled={saving} onClick={remove}>{saving ? 'Deleting...' : 'Delete employee'}</button></div></Modal>}
  </div>;
}
function FormField({ label, children }) { return <label className="form-field"><span>{label}</span>{children}</label>; }
