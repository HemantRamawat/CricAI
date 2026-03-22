import "./Header.css";

function Header() {
  return (
    <header className="header">
      <div className="logo">🏏 CricAI</div>

      <nav className="nav">
        <a href="#">Home</a>
        <a href="#">Live Matches</a>
        <a href="#">Predictions</a>
        <a href="#">About</a>
      </nav>
    </header>
  );
}

export default Header;