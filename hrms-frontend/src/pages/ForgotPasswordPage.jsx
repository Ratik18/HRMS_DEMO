import { useEffect, useRef, useState } from 'react';
import { ArrowRight, Eye, EyeOff, KeyRound, LockKeyhole, Mail } from 'lucide-react';
import { Link, useNavigate } from 'react-router-dom';
import AuthLayout from '../components/AuthLayout';
import { Alert } from '../components/Feedback';
import { authService } from '../services/authService';
import { getApiError } from '../services/api';

export default function ForgotPasswordPage() {
  const [email, setEmail] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [showNew, setShowNew] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const navigate = useNavigate();
  const timer = useRef(null);

  // stop the redirect timer if the user leaves the page early
  useEffect(() => () => clearTimeout(timer.current), []);

  const submit = async (event) => {
    event.preventDefault();
    setError(''); setSuccess('');

    if (newPassword.length < 8) { setError('Password must contain at least 8 characters'); return; }
    if (newPassword !== confirmPassword) { setError('Passwords do not match'); return; }

    setBusy(true);
    try {
      // POST /api/auth/forgot-password  { email, newPassword, confirmPassword }
      await authService.forgotPassword({ email: email.trim(), newPassword, confirmPassword });
      setSuccess('Password updated successfully. Redirecting to sign in...');
      timer.current = setTimeout(() => {
        navigate('/login', { replace: true, state: { message: 'Password reset successful. Sign in with your new password.' } });
      }, 1500);
    } catch (err) {
      setError(getApiError(err));
    } finally {
      setBusy(false);
    }
  };

  return (
    <AuthLayout>
      <div className="login-icon"><KeyRound size={23}/></div>
      <div className="eyebrow">ACCOUNT RECOVERY</div>
      <h2>Reset your password</h2>
      <p className="login-description">Enter your registered email and choose a new password.</p>
      {error && <Alert>{error}</Alert>}
      {success && <Alert type="success">{success}</Alert>}
      <form onSubmit={submit} className="login-form">
        <label className="field-label" htmlFor="email">Email address</label>
        <div className="input-with-icon"><Mail size={17}/><input id="email" type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="you@company.com" autoComplete="email" required /></div>

        <label className="field-label" htmlFor="newPassword">New password</label>
        <div className="input-with-icon"><LockKeyhole size={17}/><input id="newPassword" type={showNew ? 'text' : 'password'} value={newPassword} onChange={(e) => setNewPassword(e.target.value)} placeholder="At least 8 characters" autoComplete="new-password" minLength={8} required /><button type="button" className="password-toggle" onClick={() => setShowNew((v) => !v)} aria-label={showNew ? 'Hide password' : 'Show password'}>{showNew ? <EyeOff size={17}/> : <Eye size={17}/>}</button></div>

        <label className="field-label" htmlFor="confirmPassword">Confirm password</label>
        <div className="input-with-icon"><LockKeyhole size={17}/><input id="confirmPassword" type={showConfirm ? 'text' : 'password'} value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} placeholder="Re-enter the new password" autoComplete="new-password" minLength={8} required /><button type="button" className="password-toggle" onClick={() => setShowConfirm((v) => !v)} aria-label={showConfirm ? 'Hide password' : 'Show password'}>{showConfirm ? <EyeOff size={17}/> : <Eye size={17}/>}</button></div>

        <button className="button button-primary login-submit" type="submit" disabled={busy || Boolean(success)}>{busy ? 'Please wait...' : 'Confirm'}<ArrowRight size={17}/></button>
      </form>
      <div className="auth-switch"><Link to="/login">← Back to login</Link></div>
    </AuthLayout>
  );
}
