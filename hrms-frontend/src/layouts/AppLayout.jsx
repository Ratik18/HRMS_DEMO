import { useState } from 'react';
import { NavLink, Outlet, useLocation, useNavigate } from 'react-router-dom';
import {
  Bell, Building2, ChevronDown, ClipboardCheck, LayoutDashboard, LogOut,
  Menu, Network, Search, Users, X, BriefcaseBusiness,
} from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { initials } from '../utils/format';

const navigation = [
  { label: 'Overview', to: '/', icon: LayoutDashboard, end: true },
  { label: 'Employees', to: '/employees', icon: Users },
  { label: 'Departments', to: '/departments', icon: Building2 },
  { label: 'Designations', to: '/designations', icon: BriefcaseBusiness },
  { label: 'Onboarding', to: '/onboarding', icon: ClipboardCheck },
];

const pageMeta = {
  '/': ['Overview', 'Your people operations at a glance'],
  '/employees': ['Employees', 'Manage employee records and information'],
  '/departments': ['Departments', 'Organise people into business units'],
  '/designations': ['Designations', 'Manage roles and position titles'],
  '/onboarding': ['Onboarding', 'Track each new employee’s onboarding tasks'],
};

export default function AppLayout() {
  const [mobileOpen, setMobileOpen] = useState(false);
  const [profileOpen, setProfileOpen] = useState(false);
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const meta = pageMeta[location.pathname] || ['People workspace', 'HRMS management portal'];
  const currentDate = new Intl.DateTimeFormat('en-IN', { weekday: 'short', day: '2-digit', month: 'short', year: 'numeric' }).format(new Date());

  const handleLogout = () => { logout(); navigate('/login'); };

  return (
    <div className="app-shell">
      {mobileOpen && <button className="mobile-scrim" aria-label="Close navigation" onClick={() => setMobileOpen(false)} />}
      <aside className={`sidebar ${mobileOpen ? 'sidebar-open' : ''}`}>
        <div className="brand-row">
          <div className="brand-logo-card"><img src="/linfoi-logo.png" alt="Linfoi" /></div>
          <div className="brand-copy"><strong>LINFOI</strong><small>HR MANAGEMENT</small></div>
          <button className="mobile-close icon-button" onClick={() => setMobileOpen(false)} aria-label="Close menu"><X size={18} /></button>
        </div>
        <div className="workspace-label">WORKSPACE</div>
        <nav className="side-nav">
          {navigation.map(({ label, to, icon: Icon, end }) => (
            <NavLink key={to} to={to} end={end} onClick={() => setMobileOpen(false)} className={({ isActive }) => `nav-link ${isActive ? 'nav-link-active' : ''}`}>
              <Icon size={18} strokeWidth={1.9} /><span>{label}</span>
              {to === '/employees' && <span className="nav-pill">HR</span>}
            </NavLink>
          ))}
        </nav>
        <div className="sidebar-bottom">
          <div className="help-card"><div className="help-spark">✳</div><strong>People first.</strong><p>Keep your team information organised in one place.</p></div>
          <div className="sidebar-user">
            <div className="avatar avatar-sidebar">{initials(user?.email)}</div>
            <div className="sidebar-user-copy"><strong>{user?.email?.split('@')[0] || 'Workspace user'}</strong><small>Signed in</small></div>
            <button className="icon-button sidebar-logout" title="Sign out" onClick={handleLogout}><LogOut size={17} /></button>
          </div>
        </div>
      </aside>

      <main className="main-area">
        <header className="topbar">
          <div className="topbar-left">
            <button className="mobile-menu icon-button" onClick={() => setMobileOpen(true)} aria-label="Open menu"><Menu size={20} /></button>
            <div className="breadcrumb"><span>Workspace</span><span className="breadcrumb-sep">/</span><strong>{meta[0]}</strong></div>
          </div>
          <div className="topbar-right">
            <div className="date-label">{currentDate}</div>
            <div className="topbar-divider" />
            <button className="icon-button notification-button" aria-label="Notifications"><Bell size={18} /><i /></button>
            <div className="profile-wrap">
              <button className="profile-button" onClick={() => setProfileOpen((value) => !value)}>
                <span className="avatar avatar-small">{initials(user?.email)}</span><span className="profile-name">{user?.email || 'Account'}</span><ChevronDown size={15} />
              </button>
              {profileOpen && <div className="profile-menu"><div className="profile-menu-email">{user?.email}</div><button onClick={handleLogout}><LogOut size={15} /> Sign out</button></div>}
            </div>
          </div>
        </header>
        <div className="page-content">
          <div className="page-heading"><div><div className="eyebrow">LINFOI HRMS</div><h1>{meta[0]}</h1><p>{meta[1]}</p></div><div className="heading-decoration"><div className="decoration-dot dot-one"/><div className="decoration-dot dot-two"/><div className="decoration-dot dot-three"/></div></div>
          <Outlet />
          <footer className="app-footer"><span>LINFOI HRMS</span><span>We Staff the Minds Behind the Machines.</span></footer>
        </div>
      </main>
    </div>
  );
}
