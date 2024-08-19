import { useUser } from "@/context/UserContext";
import React, { useEffect, useRef, useState } from "react";

const Chat = () => {
  const { user } = useUser();
  const accessToken = localStorage.getItem("accessToken");
  const [messageContent, setMessageContent] = useState("");
  const [messages, setMessages] = useState<any>([]);
  const messagesEndRef = useRef<HTMLDivElement | null>(null);
  const senderId = 30;
  const receiverId = 31;
  const loggedInUserId = user?.id;

  console.log("Log in User ID", loggedInUserId);

  useEffect(() => {
    const interval = setInterval(() => {
      fetchMessages();
    }, 1500); // Poll every 1.5 seconds

    return () => clearInterval(interval);
  }, []); // Dependency array left empty to run once on mount

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const scrollToBottom = () => {
    if (messagesEndRef.current) {
      messagesEndRef.current.scrollIntoView({ behavior: "smooth" });
    }
  };

  const fetchMessages = async () => {
    if (!accessToken) {
      console.error("Access token not found");
      return;
    }

    try {
      const senderResponse = await fetch(
        `http://csci5308vm12.research.cs.dal.ca:8080/api/messages/${senderId}/${receiverId}`,
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );

      if (!senderResponse.ok) {
        throw new Error("Failed to fetch sender messages");
      }

      const senderData = await senderResponse.json();

      const receiverResponse = await fetch(
        `http://csci5308vm12.research.cs.dal.ca:8080/api/messages/${receiverId}/${senderId}`,
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );

      if (!receiverResponse.ok) {
        throw new Error("Failed to fetch receiver messages");
      }

      const receiverData = await receiverResponse.json();

      const allMessages = [...senderData, ...receiverData].sort((a, b) =>
        a.localDateTime.localeCompare(b.localDateTime)
      );

      setMessages(allMessages);
    } catch (error) {
      console.error("Error fetching messages:", error);
    }
  };

  const receiverIdOptions = [30, 31];

  const sendMessage = async () => {
    try {
      const response = await fetch(
        "http://csci5308vm12.research.cs.dal.ca:8080/api/messages/create",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${accessToken}`,
          },
          body: JSON.stringify({
            senderId: loggedInUserId,
            receiverId: receiverIdOptions.find((id) => id !== loggedInUserId),
            messageContent,
          }),
        }
      );
      console.log({ response });

      if (!response.ok) {
        throw new Error("Failed to send message");
      }

      // After sending the message, clear the input field
      setMessageContent("");
    } catch (error) {
      console.error("Error sending message:", error);
    }
  };
  //   const isMessageFromLoggedInUser = (senderId: number | undefined) => {
  //     return user?.id === senderId;
  //   };

  return (
    <div className="flex flex-col h-full">
      <div className="flex-1 overflow-y-auto p-4">
        {messages.length === 0 ? (
          <div className="text-center my-20">No messages yet</div>
        ) : (
          messages.map(
            (message: {
              senderId: number | undefined;
              id: React.Key | null | undefined;
              messageContent:
                | string
                | number
                | boolean
                | React.ReactElement<
                    any,
                    string | React.JSXElementConstructor<any>
                  >
                | Iterable<React.ReactNode>
                | React.ReactPortal
                | null
                | undefined;
            }) => {
              // Assigning a dynamic background color based on the message sender

              return (
                <div
                  key={message.id}
                  className={`mb-4 flex ${
                    message.senderId === loggedInUserId
                      ? "justify-end"
                      : "justify-start"
                  }`}
                >
                  {/* Message content wrapper */}
                  <span
                    className={`max-w-3/4 p-2 rounded ${
                      message.senderId === loggedInUserId
                        ? "bg-green-500 text-white"
                        : "bg-gray-200"
                    }`}
                    style={{ display: "inline-block" }}
                  >
                    {message.messageContent}
                  </span>
                </div>
              );
            }
          )
        )}
        <div ref={messagesEndRef} />
      </div>
      <div className="border-t border-gray-200 p-4">
        <input
          type="text"
          value={messageContent}
          onChange={(e) => setMessageContent(e.target.value)}
          onKeyPress={(e) => {
            if (e.key === "Enter" && messageContent.trim()) {
              sendMessage();
            }
          }}
          className="w-full p-2 border rounded focus:outline-none"
          placeholder="Type your message..."
        />
      </div>
    </div>
  );
};

export default Chat;
