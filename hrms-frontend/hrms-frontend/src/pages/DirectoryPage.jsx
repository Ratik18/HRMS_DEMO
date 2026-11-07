import { useEffect, useState } from 'react';
import { BriefcaseBusiness, Building2, Edit3, Plus, Search, Trash2 } from 'lucide-react';
import Modal from '../components/Modal';
import { Alert, EmptyState, Loading } from '../components/Feedback';
import { departmentService, designationService } from '../services/employeeService';
import { getApiError } from '../services/api';

export default function DirectoryPage({ kind }) {
  const isDepartment = kind === 'department';
  const config = isDepartment
    ? { title: 'Departments', singular: 'department', name: 'name', Icon: Building2, service: departmentService, placeholder: 'e.g. Engineering' }
    : { title: 'Designations', singular: 'designation', name: 'title', Icon: BriefcaseBusiness, service: designationService, placeholder: 'e.g. Software Engineer' };
  const [items, setItems] = useState([]); const [loading, setLoading] = useState(true);
  const [error, setError] = useState(''); const [notice, setNotice] = useState(''); const [search, setSearch] = useState('');
  const [modalOpen, setModalOpen] = useState(false); const [editing, setEditing] = useState(null);
  const [form, setForm] = useState({ name: '', description: '' }); const [saving, setSaving] = useState(false); const [deleteTarget, setDeleteTarget] = useState(null);
  const load = async () => { setLoading(true); setError(''); try { const response = await config.service.list(); setItems(Array.isArray(response.data) ? response.data : []); } catch (err) { setError(getApiError(err)); } finally { setLoading(false); } };
  useEffect(() => { load(); }, [kind]);
  const visible = items.filter((item) => `${item[config.name] || ''} ${item.description || ''}`.toLowerCase().includes(search.toLowerCase()));
  const openCreate = () => { setEditing(null); setForm({ name: '', description: '' }); setError(''); setModalOpen(true); };
  const openEdit = (item) => { setEditing(item); setForm({ name: item[config.name] || '', description: item.description || '' }); setError(''); setModalOpen(true); };
  const save = async (event) => { event.preventDefault(); setSaving(true); setError(''); const payload = { [config.name]: form.name.trim(), description: form.description.trim() || null }; try { if (editing) await config.service.update(editing.id, payload); else await config.service.create(payload); setModalOpen(false); setNotice(`${config.singular[0].toUpperCase()}${config.singular.slice(1)} ${editing ? 'updated' : 'created'} successfully.`); await load(); } catch (err) { setError(getApiError(err)); } finally { setSaving(false); } };
  const remove = async () => { setSaving(true); setError(''); try { await config.service.remove(deleteTarget.id); setDeleteTarget(null); setNotice(`${config.singular[0].toUpperCase()}${config.singular.slice(1)} deleted.`); await load(); } catch (err) { setError(getApiError(err)); } finally { setSaving(false); } };
  const Icon = config.Icon;
  return <div className="stack-lg">
    <div className="toolbar-row"><div className="toolbar-copy"><div className="eyebrow">ORGANISATION STRUCTURE</div><h2>{config.title} <span className="count-chip">{items.length}</span></h2><p>Keep your organisation structure consistent and up to date.</p></div><button className="button button-primary" onClick={openCreate}><Plus size={17}/> Add {config.singular}</button></div>
    {notice && <Alert type="success">{notice}</Alert>}{error && <Alert>{error}</Alert>}
    <section className="panel directory-panel"><div className="table-toolbar"><div className="search-field"><Search size={17}/><input placeholder={`Search ${config.title.toLowerCase()}...`} value={search} onChange={(e) => setSearch(e.target.value)}/></div><span className="quiet-count">{visible.length} {visible.length === 1 ? 'record' : 'records'}</span></div>
      {loading ? <Loading label={`Loading ${config.title.toLowerCase()}...`}/> : visible.length ? <div className="directory-grid">{visible.map((item, index) => <article className="directory-card" key={item.id}><div className={`directory-card-icon directory-icon-${index % 4}`}><Icon size={20}/></div><div className="directory-card-top"><span className="directory-id">ID #{item.id}</span><div className="row-actions"><button className="icon-button" title={`Edit ${config.singular}`} onClick={() => openEdit(item)}><Edit3 size={16}/></button><button className="icon-button danger-icon" title={`Delete ${config.singular}`} onClick={() => setDeleteTarget(item)}><Trash2 size={16}/></button></div></div><h3>{item[config.name]}</h3><p>{item.description || 'No description added yet.'}</p></article>)}</div> : <EmptyState title={`No ${config.title.toLowerCase()} found`} description={search ? 'Try a different search term.' : `Create your first ${config.singular} to organise the employee directory.`} action={!search && <button className="button button-primary" onClick={openCreate}><Plus size={16}/> Add {config.singular}</button>}/>}
    </section>
    {modalOpen && <Modal title={`${editing ? 'Edit' : 'Add'} ${config.singular}`} subtitle={`Create a ${config.singular} record for your HRMS.`} onClose={() => setModalOpen(false)}><form className="form-stack" onSubmit={save}><label className="form-field"><span>{isDepartment ? 'Department name' : 'Designation title'}</span><input autoFocus required maxLength={100} placeholder={config.placeholder} value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })}/></label><label className="form-field"><span>Description</span><textarea rows="3" maxLength={255} placeholder="Add a short description..." value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })}/></label>{error && <Alert>{error}</Alert>}<div className="form-actions"><button className="button button-quiet" type="button" onClick={() => setModalOpen(false)}>Cancel</button><button className="button button-primary" disabled={saving}>{saving ? 'Saving...' : editing ? 'Save changes' : `Create ${config.singular}`}</button></div></form></Modal>}
    {deleteTarget && <Modal title={`Delete ${config.singular}?`} subtitle={`This will delete “${deleteTarget[config.name]}”. Records linked to it may prevent deletion.`} onClose={() => setDeleteTarget(null)}><div className="confirm-actions"><button className="button button-quiet" onClick={() => setDeleteTarget(null)}>Cancel</button><button className="button button-danger" disabled={saving} onClick={remove}>{saving ? 'Deleting...' : 'Delete'}</button></div></Modal>}
  </div>;
}
