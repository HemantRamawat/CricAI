import { useState } from "react";
import "./MatchCard.css";
import ChatBox from "./ChatBox";

function getFlagCode(team) {
  if (!team) return "un";

  const cleaned = team.toLowerCase().replace(/\s/g, "");

  const map = {
    india: "in",
    ind: "in",

    pakistan: "pk",
    pak: "pk",

    australia: "au",
    aus: "au",

    england: "gb",
    eng: "gb",

    bangladesh: "bd",
    ban: "bd",

    southafrica: "za",
    sa: "za",

    newzealand: "nz",
    nz: "nz",

    srilanka: "lk",
    sl: "lk"
  };

  return map[cleaned] || "un";
}

function MatchCard({ match }) {
  const [openChat, setOpenChat] = useState(false);
  const raw = match.rawMatchData || {};

  const score = raw.score || [];

  const winner =
    match.teamAProb > match.teamBProb
      ? match.teamA
      : match.teamB;

  return (

    <div className="match-card">

      <div className="match-header">

        <h2>{match.teamA} vs {match.teamB}</h2>

      </div>

      <div className="meta">

        <p>{raw.series}</p>
        <p>{match.venue}</p>
        <p>{match.date}</p>

      </div>

      <div className="teams">

        <div className="team">
          <div>
            <img
              className="flag"
              src={`https://flagcdn.com/w40/${getFlagCode(match.teamA)}.png`}
              alt={match.teamA}
            />
            <span>{match.teamA}</span>
          </div>
          
          <span className="prob">
            {match.teamAProb}%
          </span>

        </div>

        <div className="bar-container">

          <div
            className="bar teamA"
            style={{ width: `${match.teamAProb}%` }}
          />

        </div>

        <div className="team">
          <div>
            <img
              className="flag"
              src={`https://flagcdn.com/w40/${getFlagCode(match.teamB)}.png`}
              alt={match.teamA}
            />
            <span>{match.teamB}</span>
          </div>
          <span className="prob">
            {match.teamBProb}%
          </span>

        </div>

        <div className="bar-container">

          <div
            className="bar teamB"
            style={{ width: `${match.teamBProb}%` }}
          />

        </div>

      </div>

      {score.length > 0 && (

        <div className="score">

          {score.map((s, i) => (
            <p key={i}>
              {s.inning}: {s.r}/{s.w} ({s.o})
            </p>
          ))}

        </div>

      )}

      <div className="prediction">

        Predicted Winner: <strong>{winner}</strong>

      </div>

      <div className="badge">
        <strong>{match.status}</strong>
      </div>

      <div className="askAI">
        <button className="askAI_button" onClick={() => setOpenChat(true)}>
          🤖 Ask AI
        </button>

        {openChat && <ChatBox onClose={() => setOpenChat(false)} matchData={match} />}
      </div>

    </div>
  );
}

export default MatchCard;