import React, { useState, useEffect } from "react";
import { Button, Modal, Select, SelectItem, DatePicker, ModalHeader, ModalBody, ModalFooter, ModalContent } from "@nextui-org/react";
import { useAppState } from "../AppStateContext.tsx";
import { RestaurantModel } from "../model/RestaurantModel.ts";
import { DeliveryDetails, DeliveryLocation } from "../utils/types.ts";
import { now, getLocalTimeZone } from "@internationalized/date";
import {uuidv4} from "../utils/apiUtils.ts";

type NewGroupOrderProps = {
    restaurantModel: RestaurantModel;
    setIsNewGroupOrderModalOpen: (isOpen: boolean) => void;
    onSubmit: (deliveryDetails: DeliveryDetails | null) => void;
};

export const NewGroupOrder: React.FC<NewGroupOrderProps> = ({ restaurantModel, setIsNewGroupOrderModalOpen, onSubmit }) => {
    const { userId } = useAppState();
    const [deliveryLocation, setDeliveryLocation] = useState<DeliveryLocation | null>(null);
    const [deliveryLocations, setDeliveryLocations] = useState<DeliveryLocation[]>([]);
    const [deliveryDate, setDeliveryDate] = useState<Date | null>(null);

    useEffect(() => {
        if (userId) {
            restaurantModel.getDeliveryLocation(userId).then((locations) => {
                setDeliveryLocations(locations);
            });
        } else {
            setIsNewGroupOrderModalOpen(false);
        }
    }, [userId, restaurantModel]);

    const handleSubmit = () => {
        if (!userId) return;
        if (deliveryLocation == null || deliveryDate == null) {
            setIsNewGroupOrderModalOpen(false);
            onSubmit(null)
        }
        else {
            const deliveryDetails: DeliveryDetails = {
                id: uuidv4(),
                deliveryLocation: deliveryLocation!,
                deliveryTime: new Date(`${deliveryDate!.toDateString()}`),
            };
            onSubmit(deliveryDetails);
            setIsNewGroupOrderModalOpen(false);
        }
    };

    return (
        <Modal
            aria-labelledby="new-group-order-modal"
            isOpen={true}
            scrollBehavior={"inside"}
            onClose={() => setIsNewGroupOrderModalOpen(false)}
        >
            <ModalContent>
                <ModalHeader>
                    <h3 id="new-group-order-modal">Create New Group Order</h3>
                </ModalHeader>

                <ModalBody>
                    <Select
                        label="Select Delivery Location"
                        value={deliveryLocation ? deliveryLocation.id : ""}
                        onChange={(value) =>
                            setDeliveryLocation(
                                deliveryLocations.find((location) => location.id === value.target.value) || null
                            )
                        }
                        aria-label="Delivery Location"
                    >
                        {deliveryLocations.map((location) => (
                            <SelectItem key={location.id} value={location.id}>
                                {location.locationName}
                            </SelectItem>
                        ))}
                    </Select>
                    <DatePicker label="Delivery Date"
                                hideTimeZone
                                showMonthAndYearPickers
                                defaultValue={now(getLocalTimeZone())}
                                onChange={(date) => {
                                    setDeliveryDate(date.toDate())
                                }}
                    />
                </ModalBody>

                <ModalFooter>
                    <Button variant={"bordered"} color={"danger"} onClick={() => setIsNewGroupOrderModalOpen(false)}>
                        Close
                    </Button>
                    <Button variant={"bordered"} color={"default"} onClick={handleSubmit}>Create Order</Button>
                </ModalFooter>
            </ModalContent>
        </Modal>
    );
};
