import AppRoutes from "./Routes/MainNavigation";
import { BrowserRouter } from "react-router-dom";
import { UserProvider } from "./context/UserContext";

function App() {
  return (
    <>
      <UserProvider>
        <BrowserRouter>
          <AppRoutes />
        </BrowserRouter>
      </UserProvider>
    </>
  );
}

export default App;
