import React, {useEffect, useState} from "react";
import {Button, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader, Select, SelectItem} from "@nextui-org/react";
import {useAppState} from "../AppStateContext.tsx";
import {CampusUser} from "../utils/types.ts";
import {RestaurantModel} from "../model/RestaurantModel.ts";

export type LoginProps = {
    restaurantModel: RestaurantModel;
};

const Login: React.FC<LoginProps> = ({restaurantModel}) => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedUserId, setSelectedUserId] = useState<string | null>(null);
    const {setUserId} = useAppState();
    const [users, setUsers] = useState<CampusUser[]>([]);

    useEffect(() => {
        restaurantModel.getAllCampusUsers().then((users) => {
            setUsers(users);
        });
    }, [restaurantModel]);

    const handleLoginLogout = () => {
        if (selectedUserId) {
            setUserId(selectedUserId); // Set the selected userId
            setIsModalOpen(false); // Close the modal
        } else {
            setIsModalOpen(true); // Open modal to select user
        }
    };

    return (
        <>
            <Button variant={"bordered"} color={"default"} onClick={handleLoginLogout} radius="full">
                {selectedUserId ? "Logout" : "Login"}
            </Button>

            {/* Modal for selecting user */}
            <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)}>
                <ModalContent>
                    <ModalHeader>Select User</ModalHeader>
                    <ModalBody>
                        <Select
                            label="Select User"
                            value={selectedUserId || ''}
                            onChange={(event) => setSelectedUserId(event.target.value)}
                        >
                            {users.map(user => (
                                <SelectItem key={user.id} value={user.id}>{user.name}</SelectItem>
                            ))}
                        </Select>
                    </ModalBody>
                    <ModalFooter>
                        <Button color="danger" onClick={() => {
                            setIsModalOpen(false);
                            setSelectedUserId(null);
                        }}>
                            Cancel
                        </Button>
                        <Button onClick={handleLoginLogout}>
                            Confirm
                        </Button>
                    </ModalFooter>
                </ModalContent>
            </Modal>
        </>
    );
};

export default Login;
