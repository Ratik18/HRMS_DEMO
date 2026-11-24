import { LockKeyhole, ShieldCheck, UsersRound } from 'lucide-react';
import { Link } from 'react-router-dom';

// Shared split-screen layout used by the Login and Forgot Password pages:
// logo + hero text on the left, header + form + footer on the right.
export default function AuthLayout({ children }) {
  return (
    <div className="login-screen">
      <div className="login-visual">
        <div className="login-brand">
          <Link to="/login" className="brand-link" title="Go to login page">
            <div className="brand-logo-card"><img src="/linfoi-logo.png" alt="Linfoi" /></div>
            <div className="brand-copy"><strong>LINFOI</strong><small>HR MANAGEMENT</small></div>
          </Link>
        </div>
        <div className="login-visual-content">
          <div className="visual-kicker"><span /> PEOPLE. PROGRESS. POSSIBILITY.</div>
          <h1>Great teams<br />start with <em>great</em><br />people practices.</h1>
          <p>A calmer, clearer way to manage your people operations—from the first day to every milestone after.</p>
          <div className="visual-points"><div><UsersRound size={17} /><span>One place for your people</span></div><div><ShieldCheck size={17} /><span>Thoughtful, secure access</span></div></div>
        </div>
        <div className="login-visual-footer"><span>BUILT AROUND YOUR PEOPLE</span></div>
        <div className="visual-orbit orbit-a"/><div className="visual-orbit orbit-b"/><div className="visual-orbit orbit-c"/>
      </div>
      <div className="login-panel">
        <div className="login-panel-top"><span>WELCOME TO LINFOI HRMS</span><span className="secure-label"><LockKeyhole size={13} /> SECURE ACCESS</span></div>
        <div className="login-form-wrap">{children}</div>
        <div className="login-panel-footer"><span>© {new Date().getFullYear()} LINFOI PRIVATE LIMITED</span><span>We Staff the Minds Behind the Machines</span></div>
      </div>
    </div>
  );
}
