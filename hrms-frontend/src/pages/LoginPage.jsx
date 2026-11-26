import { useState } from 'react';
import { ArrowRight, Eye, EyeOff, LockKeyhole, Mail, ShieldCheck, UsersRound } from 'lucide-react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { getApiError } from '../services/api';
import { Alert } from '../components/Feedback';
import AuthLayout from '../components/AuthLayout';
import { typedOnly } from '../utils/passwordGuard';

export default function LoginPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const { login, register } = useAuth();
  const [mode, setMode] = useState('login');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState('');
  // message sent from the Forgot Password page after a successful reset
  const [success, setSuccess] = useState(location.state?.message || '');

  const warnPaste = () => setError('Pasting is disabled. Please type your password.');

  const submit = async (event) => {
    event.preventDefault(); setError(''); setSuccess(''); setBusy(true);
    try {
      if (mode === 'register') {
        await register({ email: email.trim(), password });
        setSuccess('Account created successfully. Sign in with your new credentials.');
        setMode('login'); setPassword('');
      } else {
        await login({ email: email.trim(), password });
        navigate(location.state?.from?.pathname || '/', { replace: true });
      }
    } catch (err) { setError(getApiError(err)); }
    finally { setBusy(false); }
  };

  return (
    <AuthLayout>
      <div className="login-icon"><UsersRound size={23}/></div>
      <div className="eyebrow">YOUR WORKSPACE AWAITS</div>
      <h2>{mode === 'login' ? 'Welcome back' : 'Create your account'}</h2>
      <p className="login-description">{mode === 'login' ? 'Sign in to continue to your HRMS workspace.' : 'Register an account to access the HRMS workspace.'}</p>
      {error && <Alert>{error}</Alert>}
      {success && <Alert type="success">{success}</Alert>}
      <form onSubmit={submit} className="login-form">
        <label className="field-label" htmlFor="email">Email address</label>
        <div className="input-with-icon"><Mail size={17}/><input id="email" type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="you@company.com" autoComplete="email" required /></div>
        <div className="field-label-row"><label className="field-label" htmlFor="password">Password</label>{mode === 'login' && <Link to="/forgot-password" className="forgot-link">Forgot password?</Link>}</div>
        <div className="input-with-icon"><LockKeyhole size={17}/><input id="password" {...typedOnly(warnPaste)} type={showPassword ? 'text' : 'password'} value={password} onChange={(e) => setPassword(e.target.value)} placeholder={mode === 'register' ? 'At least 8 characters' : 'Enter your password'} autoComplete={mode === 'login' ? 'current-password' : 'new-password'} minLength={mode === 'register' ? 8 : undefined} required /><button type="button" className="password-toggle" onClick={() => setShowPassword((v) => !v)} aria-label={showPassword ? 'Hide password' : 'Show password'}>{showPassword ? <EyeOff size={17}/> : <Eye size={17}/>}</button></div>
        <button className="button button-primary login-submit" type="submit" disabled={busy}>{busy ? 'Please wait...' : mode === 'login' ? 'Sign in to workspace' : 'Create account'}<ArrowRight size={17}/></button>
      </form>
      <div className="auth-switch">{mode === 'login' ? 'New to this workspace?' : 'Already registered?'} <button onClick={() => { setMode(mode === 'login' ? 'register' : 'login'); setError(''); setSuccess(''); }}>{mode === 'login' ? 'Create an account' : 'Sign in'}</button></div>
      <div className="login-note"><ShieldCheck size={15}/> Your session is stored for this browser tab.</div>
    </AuthLayout>
  );
}