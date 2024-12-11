import {
    CampusUser,
    DeliveryDetails,
    DeliveryLocation,
    GroupOrder,
    IndividualOrder,
    Order,
    Restaurant
} from "../utils/types.ts";
import {API_BASE_URL} from "../utils/apiUtils.ts";

export class RestaurantModel {
    public async getCampusUserById(userId: string) {
        const response = await fetch(`${API_BASE_URL}/campus-users/${userId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        if (response.status !== 200) {
            throw new Error('Failed to fetch campus user')
        }
        return await response.json();
    }

    public async getGroupOrder(userId: string | null, groupOrderId: string): Promise<GroupOrder> {
        const response = await fetch(`${API_BASE_URL}/${userId}/group-order/${groupOrderId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        if (response.status !== 200) {
            throw new Error('Failed to fetch group order')
        }
        return await response.json() as GroupOrder;
    }

    public async validateOrder(userId: string, orderId: string) {
        const response = await fetch(`${API_BASE_URL}/${userId}/orders/${orderId}/validate`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        if (response.status !== 200) {
            throw new Error('Failed to validate order')
        }
        return await response.text();
    }

    public async validateGroupOrder(userId: string, groupOrderId: string) {
        const response = await fetch(`${API_BASE_URL}/${userId}/group-order/${groupOrderId}/validate`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        if (response.status !== 200) {
            throw new Error('Failed to validate group order')
        }
        return await response.text();
    }

    public async getRestaurantsTypes(): Promise<string[]> {
        const response = await fetch(`${API_BASE_URL}/restaurants/types`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        })
        if (response.status !== 200) {
            return []
        }
        return await response.json()
    }

    public async getAllRestaurants(): Promise<Restaurant[]> {
        const response = await fetch(`${API_BASE_URL}/restaurants/all`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        })
        if (response.status !== 200) {
            return []
        }
        const data = await response.json();
        // Transform the data into the format we need
        return data.map((restaurant: any) => ({
            id: restaurant.id,
            name: restaurant.restaurantName,
            dishes: restaurant.menu.items.map((item: any) => ({
                id: item.id,
                name: item.name,
                prepTime: item.prepTime,
                price: item.price,
                type: item.type
            })),
            openingHours: restaurant.openingTime ? this.parseLocalDateTimeArray(restaurant.openingTime) : null,
            closingHours: restaurant.closingTime ? this.parseLocalDateTimeArray(restaurant.closingTime) : null
        }));
    }

    public async getRestaurantById(id: string): Promise<Restaurant | null> {
        const response = await fetch(`${API_BASE_URL}/restaurants/${id}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        })
        if (response.status !== 200) {
            return null;
        }
        const restaurant = await response.json()
        // Transform the data into the format we need
        return {
            id: restaurant.id,
            name: restaurant.restaurantName,
            dishes: restaurant.menu.items.map((item: any) => ({
                id: item.id,
                name: item.name,
                prepTime: item.prepTime,
                price: item.price,
                type: item.type
            })),
            openingHours: restaurant.openingTime ? this.parseLocalDateTimeArray(restaurant.openingTime) : null,
            closingHours: restaurant.closingTime ? this.parseLocalDateTimeArray(restaurant.closingTime) : null
        }
    }

    public async getRestaurantByFoodType(foodType: string): Promise<Restaurant[]> {
        const response = await fetch(`${API_BASE_URL}/restaurants/foodType/${foodType}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        })
        if (response.status !== 200) {
            return []
        }
        const data = await response.json()
        // Transform the data into the format we need
        return data.map((restaurant: any) => ({
            id: restaurant.id,
            name: restaurant.restaurantName,
            dishes: restaurant.menu.items.map((item: any) => ({
                id: item.id,
                name: item.name,
                prepTime: item.prepTime,
                price: item.price,
                type: item.type
            })),
            openingHours: restaurant.openingTime ? this.parseLocalDateTimeArray(restaurant.openingTime) : null,
            closingHours: restaurant.closingTime ? this.parseLocalDateTimeArray(restaurant.closingTime) : null
        }));
    }

    public async getRestaurantsByName(name: string): Promise<Restaurant[]> {
        const response = await fetch(`${API_BASE_URL}/restaurants/name/${name}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        })
        if (response.status !== 200) {
            return []
        }
        const data = await response.json()
        // Transform the data into the format we need
        return data.map((restaurant: any) => ({
            id: restaurant.id,
            name: restaurant.restaurantName,
            dishes: restaurant.menu.items.map((item: any) => ({
                id: item.id,
                name: item.name,
                prepTime: item.prepTime,
                price: item.price,
                type: item.type
            })),
            openingHours: restaurant.openingTime ? this.parseLocalDateTimeArray(restaurant.openingTime) : null,
            closingHours: restaurant.closingTime ? this.parseLocalDateTimeArray(restaurant.closingTime) : null
        }));
    }

    public async addItemToOrder(userId: string, restaurantId: string, orderId: string, dishId: string, groupOrderId: string | null, deliveryDetails: DeliveryDetails | null): Promise<string> {
        // if one is null don't send it
        let body = null;
        if (groupOrderId != null && deliveryDetails != null) {
            body = JSON.stringify({groupOrderId: groupOrderId, deliveryDetails: deliveryDetails});
        } else if (groupOrderId != null) {
            body = JSON.stringify({groupOrderId: groupOrderId});
        } else if (deliveryDetails != null) {
            body = JSON.stringify({deliveryDetails: deliveryDetails});
        }
        const response = await fetch(`${API_BASE_URL}/${userId}/restaurants/${restaurantId}/orders/${orderId}/item/${dishId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: body
        })
        if (response.status !== 201) {
            throw new Error('Failed to add item to order')
        }
        return await response.text();
    }

    public async cancelOrder(userId: string, restaurantId: string, orderId: string): Promise<void> {
        const response = await fetch(`${API_BASE_URL}/${userId}/restaurants/${restaurantId}/orders/${orderId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        if (response.status !== 200) {
            throw new Error('Failed to cancel order')
        }
    }

    public async getOrder(userId: string, orderId: string): Promise<Order | IndividualOrder> {
        console.log("getOrder", userId, orderId)
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
        console.log(data)
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
        }
    }

    public async getOrders(userId: string): Promise<Order[]> {
        const response = await fetch(`${API_BASE_URL}/${userId}/orders`, {
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
        return data.map((order: any) => ({
            id: order.id,
            restaurantId: order.restaurantId,
            userId: order.userId,
            items: order.items.map((item: any) => ({
                id: item.id,
                name: item.name,
                prepTime: item.prepTime,
                price: item.price
            })),
            status: order.status
        }))
    }

    public async getTotalPriceOrder(userId: string, restaurantId: string, orderId: string): Promise<number> {
        const response = await fetch(`${API_BASE_URL}/${userId}/restaurants/${restaurantId}/orders/${orderId}/total-price`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        if (response.status !== 200) {
            throw new Error('Failed to fetch orders')
        }
        return await response.json();
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
                id: "0707d60a-b3aa-4616-8d08-57a041f3caf7",
                locationName: "Home",
                address: "1234 Home St",
            },
            {
                id: "65e4de55-75ab-4022-9375-12779748f89c",
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
            id: "0707d60a-b3aa-4616-8d08-57a041f3caf7",
            locationName: "Home",
            address: "1234 Home St",
        }
    }

    public async createGroupOrder(userId: string, deliveryDetails: DeliveryDetails | null): Promise<string> {
        const response = await fetch(`${API_BASE_URL}/${userId}/group-order`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: deliveryDetails != null ? JSON.stringify(deliveryDetails) : null
        })
        if (response.status !== 201) {
            throw new Error('Failed to create group order')
        }
        return await response.json();
    }

    public async joinGroupOrder(userId: string, groupOrderId: string | null) {
        const response = await fetch(`${API_BASE_URL}/${userId}/group-order/${groupOrderId}/join`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        if (response.status !== 201) {
            throw new Error('Failed to join group order')
        }
        return await response.json();
    }

    public async getAllCampusUsers(): Promise<CampusUser[]> {
        const response = await fetch(`${API_BASE_URL}/campus-users`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        })
        if (response.status !== 200) {
            return []
        }
        const data = await response.json();
        return data as CampusUser[]
    }

    /**
     * Parses an array of numbers into a JavaScript Date object.
     * @param {number[]} arr - Array containing [year, month, day, hour, minute, second, millisecond]
     * @returns {Date} - Parsed JavaScript Date object
     */
    private parseLocalDateTimeArray(arr: number[]): Date {
        const [year, month, day, hours, minutes, seconds, nanoseconds] = arr;
        const milliseconds = Math.floor(nanoseconds / 1_000_000); // Convert nanoseconds to milliseconds
        const date = new Date(year, month - 1, day, hours, minutes, seconds, milliseconds);
        return date;
    }
}
