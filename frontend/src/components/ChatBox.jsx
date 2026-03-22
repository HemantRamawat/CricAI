import { useState, useRef, useEffect } from "react";
import "./ChatBox.css"; // ✅ import css

function ChatBox({ onClose, matchData }) {

  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");
  const [loading, setLoading] = useState(false);
  const chatRef = useRef();

  const sendMessage = async () => {
    if (!input.trim()) return;

    const userMsg = { role: "user", text: input };
    const updatedMessages = [...messages, userMsg];

    setMessages(updatedMessages);
    setInput("");
    setLoading(true);

    try {
      // const res = await fetch("http://localhost:8080/api/chat", {
      //   method: "POST",
      //   headers: {
      //     "Content-Type": "application/json"
      //   },
      //   body: JSON.stringify({
      //     message: input,
      //     matchName: matchData?.matchName,
      //     history: updatedMessages.slice(-6)
      //   })
      // });

      const res = await fetch("http://localhost:8080/api/agent", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          message: input,
          matchName: matchData?.matchName
        })
      });

      const data = await res.text();

      const botMsg = { role: "bot", text: data };

      setMessages(prev => [...prev, botMsg]);
    } catch (err) {
      setMessages(prev => [
        ...prev,
        { role: "bot", text: "⚠️ Error fetching response" }
      ]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    chatRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  return (
    <div className="chat-container">

      {/* Header */}
      <div className="chat-header">
        <span>🏏 Match Assistant</span>
        <button className="chat-close-btn" onClick={onClose}>✕</button>
      </div>

      {/* Chat */}
      <div className="chat-area">
        {messages.map((m, i) => (
          <div key={i} className={`chat-row ${m.role}`}>
            <div className="chat-bubble">
              {m.text}
            </div>
          </div>
        ))}

        {loading && (
          <div className="chat-row bot">
            <div className="chat-bubble typing">
              <span></span>
              <span></span>
              <span></span>
            </div>
          </div>
        )}

        <div ref={chatRef} />
      </div>

      {/* Input */}
      <div className="chat-input-area">
        <input
          className="chat-input"
          value={input}
          placeholder={loading ? "AI is thinking..." : "Ask about this match..."}
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={(e) => e.key === "Enter" && sendMessage()}
        />
        <button
          className="chat-send-btn"
          onClick={sendMessage}
          disabled={loading}
        >
          ➤
        </button>
      </div>

    </div>
  );
}

export default ChatBox;