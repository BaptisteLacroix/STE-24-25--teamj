import React, {createContext, ReactNode, useContext, useEffect, useState} from 'react';

interface AppStateContextType {
    userId: string | null;
    setUserId: (userId: string | null) => void;
    orderId: string | null;
    setOrderId: (orderId: string | null) => void;
    groupOrderId: string | null;
    setGroupOrderId: (groupOrderId: string | null) => void;
    orderValidated: boolean;
    setOrderValidated: (orderValidated: boolean) => void;
}

const AppStateContext = createContext<AppStateContextType | undefined>(undefined);

export const useAppState = () => {
    const context = useContext(AppStateContext);
    if (!context) {
        throw new Error('useAppState must be used within an AppStateProvider');
    }
    return context;
};


export const AppStateProvider: React.FC<{ children: ReactNode }> = ({children}) => {
    const [userId, setUserId] = useState<string | null>(() => localStorage.getItem('userId') || null);
    const [orderId, setOrderId] = useState<string | null>(() => localStorage.getItem('orderId') || null);
    const [groupOrderId, setGroupOrderId] = useState<string | null>(() => localStorage.getItem('groupOrderId') || null);
    const [orderValidated, setOrderValidated] = useState<boolean>(false);

    // Store orderValidated in localStorage to persist across sessions (optional)
    useEffect(() => {
        if (orderValidated) {
            localStorage.setItem('orderValidated', 'true');
        } else {
            localStorage.removeItem('orderValidated');
        }
    }, [orderValidated]);

    useEffect(() => {
        if (userId !== null) {
            localStorage.setItem('userId', userId);
        } else {
            localStorage.removeItem('userId');
        }
    }, [userId]);

    useEffect(() => {
        if (orderId !== null) {
            localStorage.setItem('orderId', orderId);
        } else {
            localStorage.removeItem('orderId');
        }
    }, [orderId]);

    useEffect(() => {
        if (groupOrderId !== null) {
            localStorage.setItem('groupOrderId', groupOrderId);
        } else {
            localStorage.removeItem('groupOrderId');
        }
    }, [groupOrderId]);

    // Monitor userId changes - reset orderId and groupOrderId if userId becomes null
    useEffect(() => {
        if (userId === null) {
            localStorage.removeItem('orderId');
            localStorage.removeItem('groupOrderId');
            localStorage.removeItem('orderValidated');
        }
    }, [userId]);

    return (
        <AppStateContext.Provider
            value={{userId, setUserId, orderId, setOrderId, groupOrderId, setGroupOrderId, orderValidated, setOrderValidated}}
        >
            {children}
        </AppStateContext.Provider>
    );
};
