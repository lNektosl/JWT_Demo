import React, { useEffect, useState } from "react";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import type { IMessage } from "@stomp/stompjs";

const Chat: React.FC = () => {
  const [client, setClient] = useState<Client | null>(null);
  const [messages, setMessages] = useState<string[]>([]);
  const [input, setInput] = useState<string>("");

  const token =
    "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhOTcyNjUwOC0wY2Q1LTQ1MmItOThlYi02ODBlNDcyNjk3YTIiLCJyb2xlcyI6WyJST0xFX0dVRVNUIl0sImlhdCI6MTc3NzM4NDU2MCwiZXhwIjoxNzc3Mzg4MTYwfQ.TlqUzkagPquUm_fwZYC4_W_g8vNuIjRfeP-Q4rk323Y";
  useEffect(() => {
    const socket = new SockJS("http://localhost:8080/ws");

    const stompClient = new Client({
      webSocketFactory: () => socket,
      connectHeaders: {
        Authorization: `Bearer ${token}`,
      },
      onConnect: () => {
        console.log("Connected");

        stompClient.subscribe("/topic/messages", (msg: IMessage) => {
          setMessages((prev) => [...prev, msg.body]);
        });
      },
      onStompError: (frame) => {
        console.error("STOMP Error:", frame);
      },
    });

    stompClient.activate();
    setClient(stompClient);

    return () => {
      stompClient.deactivate();
    };
  }, []);

  const sendMessage = () => {
    if (client && input) {
      client.publish({
        destination: "/app/sendMessage",
        body: JSON.stringify({
          content: input,
          sender: "Nektos",
        }),
      });
      setInput("");
    }
  };

  return (
    <div>
      <h2>WebSocket Chat</h2>

      <div
        style={{
          border: "1px solid black",
          height: "200px",
          overflowY: "auto",
          marginBottom: "10px",
        }}
      >
        {messages.map((msg, idx) => (
          <div key={idx}>{msg}</div>
        ))}
      </div>

      <input
        value={input}
        onChange={(e) => setInput(e.target.value)}
        placeholder="Type message..."
      />
      <button onClick={sendMessage}>Send</button>
    </div>
  );
};

export default Chat;
