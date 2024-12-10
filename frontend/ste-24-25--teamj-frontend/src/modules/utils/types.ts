export type Restaurant = {
    id: string
    name: string
    dishes: Dish[],
    openingHours: Date | null
    closingHours: Date | null
}

export type Order = {
    id: string,
    restaurantId: string,
    userId: string,
    items: Dish[],
    status: OrderStatus
}

export type IndividualOrder = Order & {
    deliveryDetails: DeliveryDetails
}

export type Dish = {
    id: string
    name: string
    prepTime: number
    price: number
    type: string
}

export type GroupOrder = {
    id: string,
    orders: Order[],
    users: string[],
    deliveryDetails: DeliveryDetails,
    status: OrderStatus
}

export type DeliveryDetails = {
    id: string,
    deliveryLocation: DeliveryLocation,
    deliveryTime: Date,
}

export type DeliveryLocation = {
    id: string,
    locationName: string,
    address: string,
}

export enum OrderStatus {
    PENDING,
    VALIDATED,
    DELIVERED,
    CANCELLED
}
