import { useState } from 'react';
import { Input, Button, Autocomplete, AutocompleteItem } from '@nextui-org/react';

const mockUsers = [
    { label: 'Alice', key: 'alice' },
    { label: 'Bob', key: 'bob' },
    { label: 'Charlie', key: 'charlie' },
    { label: 'David', key: 'david' }
];

export function CreateGroupOrder() {
    const [deliveryLocation, setDeliveryLocation] = useState('');
    const [deliveryTime, setDeliveryTime] = useState('');
    const [users, setUsers] = useState([{ name: '' }]);

    const handleAddUser = () => {
        setUsers([...users, { name: '' }]);
    };

    const handleUserChange = (index: number, value: string) => {
        const newUsers = [...users];
        newUsers[index].name = value;
        setUsers(newUsers);
    };

    const handleRemoveUser = (index: number) => {
        setUsers(users.filter((_, i) => i !== index));
    };

    return (
        <div className="max-w-4xl mx-auto">
            <form className="p-4 space-y-4">
                <div>
                    <label className="block text-sm font-medium text-gray-700">Delivery Location</label>
                    <Input
                        fullWidth
                        value={deliveryLocation}
                        onChange={(e) => setDeliveryLocation(e.target.value)}
                    />
                </div>
                <div>
                    <label className="block text-sm font-medium text-gray-700">Delivery Time</label>
                    <Input
                        type="datetime-local"
                        fullWidth
                        value={deliveryTime}
                        onChange={(e) => setDeliveryTime(e.target.value)}
                    />
                </div>
                <div>
                    <label className="block text-sm font-medium text-gray-700">Add Users (optional)</label>
                    <div className='mt-5 flex flex-col justify-center gap-5 items-center'>
                        {users.map((user, index) => (
                            <div key={index} className="flex items-center space-x-2">
                                <Autocomplete
                                    className="w-full"
                                    defaultItems={mockUsers}
                                    label={`User ${index + 1}`}
                                    placeholder="Search a user"
                                    value={user.name}
                                    onChange={(e) => handleUserChange(index, e.target.value)}
                                >
                                    {(item) => <AutocompleteItem key={item.key}>{item.label}</AutocompleteItem>}
                                </Autocomplete>
                                <Button isIconOnly onClick={() => handleRemoveUser(index)}>
                                    <img src="/remove.svg" alt="remove" />
                                </Button>
                            </div>
                        ))}
                        <Button isIconOnly onClick={handleAddUser} className="mt-2">
                            <img src="/add.svg" alt="" />
                        </Button>
                        <Button type="submit" className="mt-4">
                            Submit
                        </Button>
                    </div>
                </div>

            </form>
        </div>
    );
}