import { useEffect, useState } from 'react';
import { Check, ChevronDown, Circle, ClipboardCheck, Plus, RefreshCw } from 'lucide-react';
import Modal from '../components/Modal';
import { Alert, EmptyState, Loading } from '../components/Feedback';
import { employeeService, onboardingService } from '../services/employeeService';
import { getApiError } from '../services/api';
import { employeeName, formatDate, getPageData, initials } from '../utils/format';

const statuses = ['PENDING', 'IN_PROGRESS', 'COMPLETED'];

export default function OnboardingPage() {
  const [employees, setEmployees] = useState([]); const [employeeId, setEmployeeId] = useState('');
  const [tasks, setTasks] = useState([]); const [loadingEmployees, setLoadingEmployees] = useState(true); const [loadingTasks, setLoadingTasks] = useState(false);
  const [error, setError] = useState(''); const [notice, setNotice] = useState(''); const [filter, setFilter] = useState('ALL');
  const [modalOpen, setModalOpen] = useState(false); const [saving, setSaving] = useState(false); const [form, setForm] = useState({ taskName: '', description: '' });

  const loadEmployees = async () => { setLoadingEmployees(true); setError(''); try { const response = await employeeService.list({ page: 0, size: 100 }); const data = getPageData(response).content; setEmployees(data); if (data.length && !employeeId) setEmployeeId(String(data[0].id)); } catch (err) { setError(getApiError(err)); } finally { setLoadingEmployees(false); } };
  const loadTasks = async (id = employeeId) => { if (!id) { setTasks([]); return; } setLoadingTasks(true); setError(''); try { const response = filter === 'ALL' ? await onboardingService.listForEmployee(id) : await onboardingService.listForEmployeeByStatus(id, filter); setTasks(Array.isArray(response.data) ? response.data : []); } catch (err) { setError(getApiError(err)); } finally { setLoadingTasks(false); } };
  useEffect(() => { loadEmployees(); }, []);
  useEffect(() => { if (employeeId) loadTasks(employeeId); }, [employeeId, filter]);

  const createTask = async (event) => { event.preventDefault(); setSaving(true); setError(''); try { await onboardingService.create(employeeId, form.taskName.trim(), form.description.trim()); setModalOpen(false); setForm({ taskName: '', description: '' }); setNotice('Onboarding task created.'); await loadTasks(); } catch (err) { setError(getApiError(err)); } finally { setSaving(false); } };
  const updateStatus = async (task, status) => { setError(''); try { await onboardingService.updateStatus(task.id, status); setNotice('Task status updated.'); await loadTasks(); } catch (err) { setError(getApiError(err)); } };
  const selectedEmployee = employees.find((employee) => String(employee.id) === String(employeeId));
  const completed = tasks.filter((task) => task.status === 'COMPLETED').length;
  const progress = tasks.length ? Math.round((completed / tasks.length) * 100) : 0;

  return <div className="stack-lg">
    <div className="toolbar-row"><div className="toolbar-copy"><div className="eyebrow">NEW JOINER EXPERIENCE</div><h2>Onboarding checklist</h2><p>Follow each employee’s tasks from first day to fully onboarded.</p></div><button className="button button-primary" disabled={!employeeId} onClick={() => { setForm({ taskName: '', description: '' }); setModalOpen(true); }}><Plus size={17}/> Add task</button></div>
    {notice && <Alert type="success">{notice}</Alert>}{error && <Alert>{error}</Alert>}
    <section className="panel onboarding-selector"><div className="selector-copy"><div className="eyebrow">SELECT AN EMPLOYEE</div><strong>View onboarding progress</strong><span>Tasks are loaded for one employee at a time, as supported by the Employee Service API.</span></div><div className="employee-select-wrap"><select value={employeeId} onChange={(e) => setEmployeeId(e.target.value)} disabled={loadingEmployees || !employees.length}><option value="">{loadingEmployees ? 'Loading employees...' : 'Select employee'}</option>{employees.map((employee) => <option key={employee.id} value={employee.id}>{employeeName(employee)} · {employee.email}</option>)}</select><ChevronDown size={16}/></div></section>
    {loadingEmployees ? <Loading label="Loading employees..."/> : !employees.length ? <EmptyState title="No employees to onboard" description="Create an employee record first, then return here to add onboarding tasks."/> : <>
      <section className="onboarding-summary"><div className="onboarding-person"><div className="avatar avatar-large">{initials(employeeName(selectedEmployee || {}))}</div><div><span className="eyebrow">ONBOARDING FOR</span><h2>{employeeName(selectedEmployee || {})}</h2><p>{selectedEmployee?.designation?.title || selectedEmployee?.designationTitle || 'Role not set'} · Joined {formatDate(selectedEmployee?.dateOfJoining)}</p></div></div><div className="progress-summary"><div className="progress-value">{progress}<small>%</small></div><div className="progress-copy"><strong>{completed} of {tasks.length} tasks completed</strong><span>Keep the first days moving smoothly.</span></div><div className="progress-track"><div style={{ width: `${progress}%` }}/></div></div></section>
      <section className="panel tasks-panel"><div className="panel-heading"><div><div className="eyebrow">TASK TRACKER</div><h2>Onboarding tasks <span className="count-chip">{tasks.length}</span></h2></div><div className="filter-tabs">{['ALL', ...statuses].map((status) => <button key={status} className={filter === status ? 'filter-tab-active' : ''} onClick={() => setFilter(status)}>{status === 'ALL' ? 'All' : status.replace('_', ' ').toLowerCase()}</button>)}</div></div>
        {loadingTasks ? <Loading label="Loading onboarding tasks..."/> : tasks.length ? <div className="task-list">{tasks.map((task) => <div className="task-row" key={task.id}><div className={`task-check task-${String(task.status).toLowerCase()}`}>{task.status === 'COMPLETED' ? <Check size={16}/> : <Circle size={13}/>}</div><div className="task-copy"><strong>{task.taskName}</strong><span>{task.description || 'No additional details'}</span></div><div className="task-status-select"><select aria-label={`Status for ${task.taskName}`} value={task.status} onChange={(e) => updateStatus(task, e.target.value)}>{statuses.map((status) => <option key={status} value={status}>{status.replace('_', ' ')}</option>)}</select></div></div>)}</div> : <EmptyState title="No onboarding tasks yet" description="Add a checklist item to start tracking this employee’s onboarding." action={<button className="button button-primary" onClick={() => setModalOpen(true)}><Plus size={16}/> Add first task</button>}/>}
      </section>
    </>}
    {modalOpen && <Modal title="Add onboarding task" subtitle={`Create a task for ${employeeName(selectedEmployee || {})}.`} onClose={() => setModalOpen(false)}><form className="form-stack" onSubmit={createTask}><label className="form-field"><span>Task name</span><input autoFocus required maxLength={200} value={form.taskName} onChange={(e) => setForm({ ...form, taskName: e.target.value })} placeholder="e.g. Complete document verification"/></label><label className="form-field"><span>Description (optional)</span><textarea rows="3" value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} placeholder="Add task details..."/></label><div className="form-actions"><button className="button button-quiet" type="button" onClick={() => setModalOpen(false)}>Cancel</button><button className="button button-primary" disabled={saving}>{saving ? 'Creating...' : 'Create task'}</button></div></form></Modal>}
  </div>;
}
