import { useEffect, useMemo, useState } from 'react';
import { Activity, ArrowUpRight, BriefcaseBusiness, Building2, CalendarDays, CircleUserRound, Users, UserPlus } from 'lucide-react';
import { Link } from 'react-router-dom';
import { departmentService, designationService, employeeService } from '../services/employeeService';
import { getApiError } from '../services/api';
import { Alert, Loading } from '../components/Feedback';
import { employeeName, formatDate, getPageData, initials } from '../utils/format';

export default function DashboardPage() {
  const [state, setState] = useState({ loading: true, error: '', employees: [], totalEmployees: 0, departments: [], designations: [] });
  useEffect(() => {
    let alive = true;
    Promise.allSettled([
      employeeService.list({ page: 0, size: 6 }),
      departmentService.list(),
      designationService.list(),
    ]).then(([employeeResult, departmentResult, designationResult]) => {
      if (!alive) return;
      const failures = [employeeResult, departmentResult, designationResult].filter((item) => item.status === 'rejected');
      const page = employeeResult.status === 'fulfilled' ? getPageData(employeeResult.value) : { content: [], totalElements: 0 };
      setState({
        loading: false,
        error: failures.length ? getApiError(failures[0].reason) : '',
        employees: page.content,
        totalEmployees: page.totalElements,
        departments: departmentResult.status === 'fulfilled' && Array.isArray(departmentResult.value.data) ? departmentResult.value.data : [],
        designations: designationResult.status === 'fulfilled' && Array.isArray(designationResult.value.data) ? designationResult.value.data : [],
      });
    });
    return () => { alive = false; };
  }, []);

  const activeCount = useMemo(() => state.employees.filter((item) => item.status === 'ACTIVE').length, [state.employees]);
  const departmentCounts = useMemo(() => {
    const counts = new Map();
    state.employees.forEach((employee) => {
      const name = employee.department?.name || employee.departmentName || 'Unassigned';
      counts.set(name, (counts.get(name) || 0) + 1);
    });
    return [...counts.entries()].sort((a, b) => b[1] - a[1]).slice(0, 5);
  }, [state.employees]);
  const maxCount = Math.max(1, ...departmentCounts.map(([, count]) => count));

  return (
    <div className="stack-lg">
      {state.error && <Alert type="info">Some dashboard data could not load: {state.error}</Alert>}
      <section className="welcome-banner">
        <div><div className="banner-kicker">YOUR PEOPLE, IN ONE VIEW</div><h2>Make every people moment count.</h2><p>A quick look at the teams and talent that keep your organisation moving.</p><div className="banner-actions"><Link to="/employees" className="button button-light">Explore employees <ArrowUpRight size={16}/></Link><Link to="/departments" className="banner-link">Manage your structure</Link></div></div>
        <div className="banner-art"><div className="banner-ring ring-one"/><div className="banner-ring ring-two"/><div className="banner-ring ring-three"/><div className="banner-art-card"><div className="mini-avatars"><span>AM</span><span>RK</span><span>JS</span><span>+8</span></div><strong>People make progress</strong><small>Build a workplace where everyone belongs.</small></div><div className="banner-star star-a">✳</div><div className="banner-star star-b">✦</div></div>
      </section>

      <div className="section-head"><div><div className="eyebrow">WORKFORCE SNAPSHOT</div><h2>At a glance</h2></div><span className="updated-label"><Activity size={14}/> Live from your HRMS APIs</span></div>
      {state.loading ? <Loading label="Loading workforce data..."/> : <div className="stats-grid">
        <StatCard icon={Users} label="Total employees" value={state.totalEmployees} foot="Across your organisation" tone="violet" />
        <StatCard icon={CircleUserRound} label="On this page · active" value={activeCount} foot="Active employees in latest results" tone="mint" />
        <StatCard icon={Building2} label="Departments" value={state.departments.length} foot="Organisational groups" tone="peach" />
        <StatCard icon={BriefcaseBusiness} label="Designations" value={state.designations.length} foot="Position titles defined" tone="blue" />
      </div>}

      <div className="dashboard-grid">
        <section className="panel recent-panel">
          <div className="panel-heading"><div><div className="eyebrow">YOUR TEAM</div><h2>Recently added people</h2></div><Link to="/employees" className="text-link">View all <ArrowUpRight size={15}/></Link></div>
          {state.loading ? <Loading/> : state.employees.length ? <div className="recent-list">{state.employees.slice(0, 5).map((employee) => <div className="recent-row" key={employee.id}><div className="avatar avatar-table">{initials(employeeName(employee))}</div><div className="recent-person"><strong>{employeeName(employee)}</strong><small>{employee.email}</small></div><div className="recent-role"><strong>{employee.designation?.title || employee.designationTitle || '—'}</strong><small>{employee.department?.name || employee.departmentName || 'No department'}</small></div><div className="recent-date">{formatDate(employee.dateOfJoining)}</div></div>)}</div> : <div className="inline-empty">No employee records yet. <Link to="/employees">Add the first employee</Link>.</div>}
        </section>
        <section className="panel distribution-panel">
          <div className="panel-heading"><div><div className="eyebrow">TEAM SHAPE</div><h2>Employees by department</h2></div><div className="panel-icon"><Building2 size={18}/></div></div>
          {state.loading ? <Loading/> : departmentCounts.length ? <div className="bar-list">{departmentCounts.map(([name, count], index) => <div className="bar-item" key={name}><div className="bar-meta"><span>{name}</span><strong>{count}</strong></div><div className="bar-track"><div className={`bar-fill bar-fill-${index}`} style={{ width: `${Math.max(7, (count / maxCount) * 100)}%` }}/></div></div>)}</div> : <div className="inline-empty">Department breakdown will appear when employees are assigned to departments.</div>}
          <Link to="/departments" className="distribution-link">Explore departments <ArrowUpRight size={15}/></Link>
        </section>
      </div>
      <section className="quick-actions"><div><div className="eyebrow">KEEP THINGS MOVING</div><h2>Quick actions</h2></div><div className="quick-action-grid"><QuickAction icon={UserPlus} title="Add employee" description="Create a new employee record" to="/employees"/><QuickAction icon={Building2} title="Add department" description="Organise your teams" to="/departments"/><QuickAction icon={CalendarDays} title="Onboarding tasks" description="Track new joiner progress" to="/onboarding"/></div></section>
    </div>
  );
}

function StatCard({ icon: Icon, label, value, foot, tone }) {
  return <div className="stat-card"><div className={`stat-icon stat-${tone}`}><Icon size={19}/></div><div className="stat-label">{label}</div><div className="stat-value">{value}</div><div className="stat-foot">{foot}</div></div>;
}
function QuickAction({ icon: Icon, title, description, to }) {
  return <Link to={to} className="quick-action"><span className="quick-icon"><Icon size={19}/></span><span><strong>{title}</strong><small>{description}</small></span><ArrowUpRight size={16} className="quick-arrow"/></Link>;
}
