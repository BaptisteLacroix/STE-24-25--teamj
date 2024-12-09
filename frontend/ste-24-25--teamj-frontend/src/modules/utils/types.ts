export type Restaurant = {
    id: string
    name: string
    dishes: Dish[]
    openingHours: Date
    closingHours: Date
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
}

export type GroupOrder = {
    id: string,
    orders: Order[],
    users: string[],
    deliveryDetails: DeliveryDetails,
    status: OrderStatus
}

export type DeliveryDetails = {
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
