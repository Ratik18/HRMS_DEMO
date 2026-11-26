// Spread this onto a password <input> so the password can only be TYPED:
// paste, drag-and-drop, copy, cut and the right-click menu are all blocked.
//
//   <input type="password" {...typedOnly(() => setError('Pasting is disabled.'))} />
//
// onBlocked (optional) runs when someone tries to paste or drop text,
// so the page can tell them why nothing happened.
export function typedOnly(onBlocked) {
  const stop = (event) => event.preventDefault();
  const stopAndWarn = (event) => {
    event.preventDefault();
    if (onBlocked) onBlocked();
  };

  return {
    onPaste: stopAndWarn,
    onDrop: stopAndWarn,
    onCopy: stop,
    onCut: stop,
    onContextMenu: stop,
  };
}