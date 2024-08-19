import React, {
  createContext,
  useContext,
  useState,
  useEffect,
  ReactNode,
} from "react";
import UserModel from "@/models/UserModel";
import userService from "@/services/user.service";

interface UserContextType {
  user: UserModel | null;
  setUser: (user: UserModel | null) => void;
  resetUser: () => void;
  loading: boolean;
  fetchUserData: () => void;
}

const UserContext = createContext<UserContextType | undefined>(undefined);

interface UserProviderProps {
  children: ReactNode;
}

export const UserProvider: React.FC<UserProviderProps> = ({ children }) => {
  const [user, setUser] = useState<UserModel | null>(null);
  const [loading, setLoading] = useState(true); // Initialize loading state

  const resetUser = () => {
    localStorage.clear();
    setUser(null);
  };

  const fetchUserData = () => {
    setLoading(true);
    const token = localStorage.getItem("accessToken");
    if (token) {
      userService
        .getUser(token)
        .then((res) => {
          setUser(res.data);
        })
        .catch((error) => {
          console.error("Failed to fetch user details", error);
        })
        .finally(() => {
          setLoading(false);
        });
    } else {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUserData();
  }, []);

  return (
    <UserContext.Provider
      value={{ user, setUser, resetUser, loading, fetchUserData }}
    >
      {children}
    </UserContext.Provider>
  );
};

// Hook to use the context
export const useUser = () => {
  const context = useContext(UserContext);
  if (context === undefined) {
    throw new Error("useUser must be used within a UserProvider");
  }
  return context;
};
