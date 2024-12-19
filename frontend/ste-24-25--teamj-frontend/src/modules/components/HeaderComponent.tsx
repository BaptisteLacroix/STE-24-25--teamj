import {Badge, Button, Input, Navbar, NavbarContent} from "@nextui-org/react";
import {ShoppingCartIcon} from "../utils/icons/ShoppingCartIcon.tsx";
import {Link} from "react-router-dom";
import {RestaurantModel} from "../model/RestaurantModel.ts";
import {useEffect, useState} from "react";
import {IndividualOrder, Order} from "../utils/types.ts";
import {SearchIcon} from "../utils/icons/SearchIcon.tsx";
import {useAppState} from "../AppStateContext.tsx";
import {CopyIcon} from "../utils/icons/CopyIcon.tsx";
import {ValidIcon} from "../utils/icons/validIcon.tsx";
import Login from "./Login.tsx";

type PolyFoodHeaderProps = {
    restaurantModel: RestaurantModel;
    setIsNewGroupOrderModalOpen: (isOpen: boolean) => void;
    setIsJoinGroupOrderModalOpen: (isOpen: boolean) => void;
    searchQuery: string;
    setSearchQuery: (query: string) => void;
    order: Order | IndividualOrder | null;
    setOrder: (order: Order | IndividualOrder) => void;
};

export function PolyfoodHeader({
                                   restaurantModel,
                                   setIsNewGroupOrderModalOpen,
                                   setIsJoinGroupOrderModalOpen,
                                   searchQuery,
                                   setSearchQuery,
                                   order,
                                   setOrder,
                               }: PolyFoodHeaderProps) {
    const {userId, orderId, groupOrderId} = useAppState();
    const [isCopied, setIsCopied] = useState(false);

    useEffect(() => {
        if (!orderId || !userId) return;

        // Check if the order is already set to avoid unnecessary fetches
        if (order && order.id === orderId) return;

        restaurantModel.getOrder(userId, orderId).then((order) => {
            setOrder(order);
        });
    }, [orderId, restaurantModel, order, userId]);

    const handleCopyGroupOrderId = () => {
        if (groupOrderId) {
            navigator.clipboard.writeText(groupOrderId).then(() => {
                setIsCopied(true);  // Set copied state to true
                setTimeout(() => {
                    setIsCopied(false);  // Revert back to CopyIcon after 5 seconds
                }, 2000);
            });
        }
    };

    return (
        <Navbar isBordered>
            <NavbarContent as="div" className="items-center flex" justify="start">
                <Link to="/">
                    <Button variant={"bordered"} color={"default"} radius="full">Home</Button>
                </Link>
                {userId !== null ? (
                    <>
                        {/* Group Order ID Button (if groupOrderId exists) */}
                        {groupOrderId && (
                            <Button
                                variant={"bordered"}
                                color={"default"}
                                className={"opacity-50"}
                                radius="full"
                                onClick={handleCopyGroupOrderId}
                                endContent={isCopied ? <ValidIcon /> : <CopyIcon />}
                            >
                                {groupOrderId}
                            </Button>
                        )}
                        {!groupOrderId && (<><Button variant={"bordered"} color={"default"}
                                                     onClick={() => setIsNewGroupOrderModalOpen(true)}
                                                     radius="full">New Group Order</Button>
                            <Button variant={"bordered"}
                                    color={"default"}
                                    radius="full"
                                    onClick={() => setIsJoinGroupOrderModalOpen(true)}>Join
                                Group Order</Button></>)}
                    </>
                ) : (
                    <>
                        <Button variant={"bordered"} color={"default"} radius="full" disabled>New Group Order</Button>
                        <Button variant={"bordered"} color={"default"} radius="full" disabled>Join Group Order</Button>
                    </>
                )}
            </NavbarContent>

            <NavbarContent as="div" className="items-center" justify="center">
                <Input
                    classNames={{
                        base: "max-w-full sm:max-w-[20rem] h-10",
                        mainWrapper: "h-full",
                        input: "text-small",
                        inputWrapper:
                            "h-full font-normal text-default-500 bg-default-400/20 dark:bg-default-500/20",
                    }}
                    placeholder="Type to search..."
                    size="lg"
                    startContent={<SearchIcon size={18}/>}
                    type="search"
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                />
            </NavbarContent>

            <NavbarContent as="div" className="items-center" justify="end">
                <Login restaurantModel={restaurantModel}/>

                <Badge color="primary" content={order?.items.length} className={"bg-none"}>
                    <Link to="/cart">
                        <Button
                            isIconOnly
                            aria-label="cart"
                            radius="full"
                            variant="light"
                        >
                            <ShoppingCartIcon className={"w-6 h-6"}/>
                        </Button>
                    </Link>
                </Badge>
            </NavbarContent>
        </Navbar>
    );
}
