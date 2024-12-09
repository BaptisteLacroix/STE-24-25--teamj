import {RestaurantModel} from "../model/RestaurantModel.ts";
import {useAppState} from "../AppStateContext.tsx";
import React, {useState} from "react";
import {Button, Input, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader} from "@nextui-org/react";

type NewGroupOrderProps = {
    restaurantModel: RestaurantModel;
    setIsJoinGroupOrderModalOpen: (isOpen: boolean) => void;
};

export const JoinGroupOrder: React.FC<NewGroupOrderProps> = ({restaurantModel, setIsJoinGroupOrderModalOpen}) => {
    const {userId} = useAppState();
    const [groupOrderId, setGroupOrderId] = useState<string | null>(null);

    const handleSubmit = () => {
        if (!userId) {
            setIsJoinGroupOrderModalOpen(false);
            return;
        }

        restaurantModel.joinGroupOrder(userId, groupOrderId).then((groupOrderId: string) => {
            setGroupOrderId(groupOrderId);
        });

        setIsJoinGroupOrderModalOpen(false);
    };

    return (
        <Modal
            aria-labelledby="join-group-order-modal"
            isOpen={true}
            scrollBehavior={"inside"}
            onClose={() => setIsJoinGroupOrderModalOpen(false)}
        >
            <ModalContent>
                <ModalHeader>
                    <h3 id="join-group-order-modal">Join Group Order</h3>
                </ModalHeader>

                <ModalBody>
                    <Input
                        placeholder="Group Order ID"
                        onChange={(e) => setGroupOrderId(e.target.value)}
                    />
                </ModalBody>

                <ModalFooter>
                    <Button variant={"bordered"} color={"danger"} onClick={() => setIsJoinGroupOrderModalOpen(false)}>
                        Close
                    </Button>
                    <Button variant={"bordered"} color={"default"} onClick={handleSubmit}>Join Group Order</Button>
                </ModalFooter>
            </ModalContent>
        </Modal>
    );
};
