import { Badge, Button, Input, Navbar, NavbarContent } from "@nextui-org/react";
import { ShoppingCartIcon } from "../utils/icons/ShoppingCartIcon.tsx";
import { Link } from "react-router-dom";
import { RestaurantModel } from "../model/RestaurantModel.ts";
import { useEffect } from "react";
import { IndividualOrder, Order } from "../utils/types.ts";
import { SearchIcon } from "../utils/icons/SearchIcon.tsx";
import { useAppState } from "../AppStateContext.tsx";

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
    const { userId, setUserId, orderId } = useAppState();

    useEffect(() => {
        if (!orderId || !userId) return;

        // Check if the order is already set to avoid unnecessary fetches
        if (order && order.id === orderId) return;

        restaurantModel.getOrder(userId, orderId).then((order) => {
            setOrder(order);
        });
    }, [orderId, restaurantModel, order, userId]);

    const handleLoginLogout = () => {
        if (userId) {
            setUserId(null);
        } else {
            setUserId("1aeb4480-305a-499d-885c-7d2d9f99153b");
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
                        <Button variant={"bordered"} color={"default"} onClick={() => setIsNewGroupOrderModalOpen(true)}
                            radius="full">New Group Order</Button>
                        <Button variant={"bordered"} color={"default"} radius="full"
                            onClick={() => setIsJoinGroupOrderModalOpen(true)}>Join Group Order</Button></>
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
                    startContent={<SearchIcon size={18} />}
                    type="search"
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                />
            </NavbarContent>

            <NavbarContent as="div" className="items-center" justify="end">
                {/* Show login/logout button */}
                <Button variant={"bordered"} color={"default"} onClick={handleLoginLogout} radius="full">
                    {userId ? "Logout" : "Login"}
                </Button>

                <Badge color="primary" content={order?.items.length} className={"bg-none"}>
                    <Link to="/cart">
                        <Button
                            isIconOnly
                            aria-label="cart"
                            radius="full"
                            variant="light"
                        >
                            <ShoppingCartIcon className={"w-6 h-6"} />
                        </Button>
                    </Link>
                </Badge>
            </NavbarContent>
        </Navbar>
    );
}
