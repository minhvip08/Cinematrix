import { useState } from "react";
import { searchUser } from "../../api/searchUser";
import { banUser, unbanUser } from "../../api/banUser";

function UserCard({props}) {
    const {id, fullName, email, isBanned, lastactiveDate, role} = props;
    const [buttonDisabled, setButtonDisabled] = useState(false);

    const handleButtonClick = async (action) => {
        try{
            if (action === 'ban') {
                await banUser(id);
            }
            else if (action === 'unban') {
                await unbanUser(id);
            }
            setButtonDisabled(true);
        }
        catch (err) {
            console.log(err);
        }
    }

    return (
        <div className="p-4 border border-solid border-white">
            <table className="table-auto space-x-3">
                <tbody>
                    <tr>
                        <td>ID</td>
                        <td>{id}</td>
                    </tr>
                    <tr>
                        <td>Name</td>
                        <td>{fullName}</td>
                    </tr>
                    <tr>
                        <td>Email</td>
                        <td>{email}</td>
                    </tr>
                    <tr>
                        <td>Last active</td>
                        <td>{lastactiveDate}</td>
                    </tr>
                    <tr>
                        <td>Role</td>
                        <td>{role}</td>
                    </tr>

                </tbody>
            </table>

            {isBanned ?
                <button className={'bg-blue-500 p-4 m-4 rounded-lg text-center text-white font-bold'} onClick={() => handleButtonClick('unban')}>Unban</button> :
                <button className={'bg-red-500 p-4 m-4 rounded-lg text-center text-white font-bold'} onClick={() => handleButtonClick('ban')}>Ban</button> 
            }
        </div>
    )
}


export function BanUser() {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [result, setResult] = useState(null);


    const handleChange = (event) => {
        event.preventDefault();
        const {name, value} = event.target;
        if (name === 'username') {
            setUsername(value);
        }
        else if (name === 'email') {
            setEmail(value);
        }
    }

    const handleSubmit = async (event) => {
        event.preventDefault();
        const users = await searchUser(username, email);
        setResult(users.data);
    }

    return (
        <>
            <form onSubmit={handleSubmit} className="flex items-center mx-auto">
                <input 
                    type="text"
                    name="username"
                    placeholder="Username..."
                    onChange={handleChange}
                    className="border border-gray-300 rounded-md p-2 mr-2 focus:outline-none focus:border-blue-500"
                />
                <input
                    type="text"
                    name="email"
                    placeholder="Email..."
                    onChange={handleChange}
                    className="border border-gray-300 rounded-md p-2 mr-2 focus:outline-none focus:border-blue-500"
                />
                <button 
                    type="submit"
                    className="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 focus:outline-none focus:bg-blue-600"
                >
                    Search
                </button>
            </form>
            {result !== null && (
                <>
                <div className="md:grid md:grid-cols-4 gap-2 my-4">
                    {result.map((user, index) => 
                        <UserCard key={index} props={user} />
                    )}
                </div>
                </>
            )}
        </>
    )
}