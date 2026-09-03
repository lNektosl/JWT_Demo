import React, { useEffect, useState } from "react";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import type { IMessage } from "@stomp/stompjs";

const Chat: React.FC = () => {
  const [client, setClient] = useState<Client | null>(null);
  const [messages, setMessages] = useState<string[]>([]);
  const [input, setInput] = useState<string>("");

  useEffect(() => {

      let stompClient :Client|null = null
 const conect = async () => {
     const res = await fetch("http://localhost:8080/api/auth/guest", {
         method: 'POST'
     })
     const token = await res.text();

     console.log(token)
     const socket = new SockJS("http://localhost:8080/ws");

     stompClient = new Client({
         webSocketFactory: () => socket,
         connectHeaders: {
             Authorization: `Bearer ${token}`,
         },
         onConnect: () => {
             console.log("Connected");

             stompClient?.subscribe("/topic/messages", (msg: IMessage) => {
                 setMessages((prev) => [...prev, msg.body]);
             });
         },
         onStompError: (frame) => {
             console.error("STOMP Error:", frame);
         },
     });

     stompClient.activate();
     setClient(stompClient);};
    conect()

    return () => {
      stompClient?.deactivate();
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
