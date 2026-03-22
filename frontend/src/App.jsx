import { useEffect, useRef, useState } from "react";
import axios from "axios";
import MatchCard from "./components/MatchCard";
import Header from "./components/Header";
import Footer from "./components/Footer";
import "./App.css";

function App() {
  const [matches, setMatches] = useState([]);
  const hasFetched = useRef(false);
  useEffect(() => {
    if (hasFetched.current) return;
    hasFetched.current = true;
    
    async function fetchMatches() {
      try {
        const res = await axios.get("http://localhost:8080/api/predictions");
        setMatches(res.data);
      } catch (err) {
        console.error("Error fetching matches:", err);
      }
    }

    fetchMatches();
  }, []);

  return (
    <div className="app">
      <Header />

      <main className="main-content">
        <h1 className="title">🏏 AI Cricket Match Predictions</h1>

        <div className="match-grid">
          {matches.length > 0 ? (
            matches.map((match, index) => (
              <MatchCard key={index} match={match} />
            ))
          ) : (
            <p className="loading">Loading matches...</p>
          )}
        </div>
      </main>

      <Footer />
    </div>
  );
}

export default App;