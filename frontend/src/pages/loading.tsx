const LoadingPage = () => {
  return (
    <div
      style={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        height: "100vh",
        flexDirection: "column",
        backgroundColor: "#f0f0f0",
      }}
    >
      <div
        style={{
          border: "6px solid #12AD2B",
          borderTop: "6px solid #fff",
          borderRadius: "50%",
          width: "50px",
          height: "50px",
          animation: "spin 2s linear infinite",
        }}
      ></div>
      <p style={{ marginTop: "20px", fontSize: "20px", color: "#3498db" }}>
        Loading...
      </p>
      <style>{`
        @keyframes spin {
          0% { transform: rotate(0deg); }
          100% { transform: rotate(360deg); }
        }
      `}</style>
    </div>
  );
};

export default LoadingPage;
