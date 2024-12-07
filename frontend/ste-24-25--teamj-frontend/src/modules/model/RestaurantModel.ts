import {DeliveryDetails, DeliveryLocation, IndividualOrder, Order, OrderStatus, Restaurant} from "../utils/types.ts";
import {RESTAURANTS_MOCK} from "../utils/mocks.ts";

export class RestaurantModel {
    public async getAllRestaurants(): Promise<Restaurant[]> {
        /*
        const response = await fetch(`${API_BASE_URL}/restaurants/all`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        if (response.status !== 200) {
            throw new Error('Failed to fetch restaurants')
        }
        const data = await response.json()
        // Transform the data into the format we need
        return data.map((restaurant: any) => ({
            id: restaurant.uuid,
            name: restaurant.restaurantName,
            openingHours: new Date(restaurant.openingHours),
            closingHours: new Date(restaurant.closingHours),
            dishes: restaurant.dishes.map((dish: any) => ({
                id: dish.id,
                name: dish.name,
                price: dish.price,
                prepTime: dish.prepTime
            }))
        }))*/
        return RESTAURANTS_MOCK
    }

    public async getRestaurantById(id: string): Promise<Restaurant> {
        /*
        const response = await fetch(`${API_BASE_URL}/restaurants/${id}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        if (response.status !== 200) {
            throw new Error('Failed to fetch restaurant')
        }
        const restaurant = await response.json()
        // Transform the data into the format we need
        return {
            id: restaurant.uuid,
            name: restaurant.restaurantName,
            openingHours: new Date(restaurant.openingHours),
            closingHours: new Date(restaurant.closingHours),
            dishes: restaurant.dishes.map((dish: any) => ({
                id: dish.id,
                name: dish.name,
                price: dish.price,
                prepTime: dish.prepTime
            }))
        }*/
        return RESTAURANTS_MOCK[0]
    }

    public async getRestaurantByFoodType(foodType: string): Promise<Restaurant[]> {
        /*
        const response = await fetch(`${API_BASE_URL}/restaurants/foodType/${foodType}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        if (response.status !== 200) {
            throw new Error('Failed to fetch restaurants')
        }
        const data = await response.json()
        // Transform the data into the format we need
        return data.map((restaurant: any) => ({
            id: restaurant.uuid,
            name: restaurant.restaurantName,
            openingHours: new Date(restaurant.openingHours),
            closingHours: new Date(restaurant.closingHours),
            dishes: restaurant.dishes.map((dish: any) => ({
                id: dish.id,
                name: dish.name,
                price: dish.price,
                prepTime: dish.prepTime
            }))
        }))*/
        return RESTAURANTS_MOCK.slice(0, 3)
    }

    public async getRestaurantsByName(name: string): Promise<Restaurant[]> {
        /*
        const response = await fetch(`${API_BASE_URL}/restaurants/name/${name}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        if (response.status !== 200) {
            throw new Error('Failed to fetch restaurants')
        }
        const data = await response.json()
        // Transform the data into the format we need
        return data.map((restaurant: any) => ({
            id: restaurant.uuid,
            name: restaurant.restaurantName,
            openingHours: new Date(restaurant.openingHours),
            closingHours: new Date(restaurant.closingHours),
            dishes: restaurant.dishes.map((dish: any) => ({
                id: dish.id,
                name: dish.name,
                price: dish.price,
                prepTime: dish.prepTime
            }))
        }))*/
        return RESTAURANTS_MOCK.slice(0, 3)
    }

    public async startNewOrder(userId: string, restaurantId: string, groupOrderId: string | null, deliveryDetails: DeliveryDetails | null): Promise<string> {
        /*
        const response = await fetch(`${API_BASE_URL}/${userId}/restaurants/${restaurantId}/order`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                userId
            })
        })
        if (response.status !== 200) {
            throw new Error('Failed to start new order')
        }
        const data = await response.json()
        return data.orderId*/
        return "1"
    }

    public async addItemToOrder(userId: string, restaurantId: string, orderId: string, dishId: string, groupOrderId: string | null, deliveryDetails: DeliveryDetails | null): Promise<void> {
        /*
        const body = JSON.stringify({
            deliveryTime: deliveryDetails?.deliveryTime || null,
            groupOrderId: groupOrderId
        })
        const response = await fetch(`${API_BASE_URL}/${userId}/restaurants/${restaurantId}/orders/${orderId}/item/${dishId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: body
        })
        if (response.status !== 200) {
            throw new Error('Failed to add item to order')
        }*/
    }

    public async cancelOrder(userId: string, restaurantId: string, orderId): Promise<void> {
        /*
        const response = await fetch(`${API_BASE_URL}/${userId}/restaurants/${restaurantId}/orders/${orderId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        if (response.status !== 200) {
            throw new Error('Failed to cancel order')
        }*/
    }

    public async getOrder(userId: string, orderId: string): Promise<Order | IndividualOrder> {
        /*
        const response = await fetch(`${API_BASE_URL}/${userId}/orders/${orderId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        if (response.status !== 200) {
            throw new Error('Failed to fetch orders')
        }
        const data = await response.json()
        // Transform the data into the format we need
        return {
            id: data.id,
            restaurantId: data.restaurantId,
            userId: data.userId,
            items: data.items.map((item: any) => ({
                id: item.id,
                name: item.name,
                prepTime: item.prepTime,
                price: item.price
            })),
            status: data.status
        }*/
        return {
            id: "1",
            restaurantId: "1",
            userId: "1",
            items: [
                {
                    id: "1",
                    name: "pastashiuta",
                    prepTime: 3,
                    price: 7
                },
                {
                    id: "2",
                    name: "pizza",
                    prepTime: 10,
                    price: 8
                }
            ],
            status: OrderStatus.PENDING
        }
    }

    public async getTotalPriceOrder(restaurantId: string, orderId: string): Promise<number> {
        /*
        const response = await fetch(`${API_BASE_URL}/restaurant/${restaurantId}/orders/${orderId}/totalPrice`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        if (response.status !== 200) {
            throw new Error('Failed to fetch orders')
        }
        const data = await response.json()
        return data.totalPrice*/
        return 15
    }

    public async getDeliveryLocation(userId: string): Promise<DeliveryLocation[]> {
        /*
        const response = await fetch(`${API_BASE_URL}/${userId}/deliveryLocation`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        if (response.status !== 200) {
            throw new Error('Failed to fetch delivery details')
        }
        const data = await response.json()
        return {
            deliveryTime: new Date(data.deliveryTime),
            deliveryAddress: data.deliveryAddress
        }*/
        return [
            {
                id: "1",
                locationName: "Home",
                address: "1234 Home St",
            },
            {
                id: "2",
                locationName: "Work",
                address: "5678 Work",
            }
        ]
    }

    public async getDeliveryLocationById(userId: string, locationId: string): Promise<DeliveryLocation> {
        /*
        const response = await fetch(`${API_BASE_URL}/${userId}/deliveryLocation/${locationId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        if (response.status !== 200) {
            throw new Error('Failed to fetch delivery details')
        }
        const data = await response.json()
        return {
            deliveryTime: new Date(data.deliveryTime),
            deliveryAddress: data.deliveryAddress
        }*/
        return {
            id: "1",
            locationName: "Home",
            address: "1234 Home St",
        }
    }

    public async createGroupOrder(userId: string, deliveryDetails: DeliveryDetails | null): Promise<string> {
        /*
        const response = await fetch(`${API_BASE_URL}/${userId}/groupOrder`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                deliveryTime: deliveryDetails.deliveryTime,
                deliveryLocation: deliveryDetails.deliveryLocation
            })
        })
        if (response.status !== 200) {
            throw new Error('Failed to create group order')
        }
        const data = await response.json()
        return data.groupOrderId*/
        return "1"
    }
}
