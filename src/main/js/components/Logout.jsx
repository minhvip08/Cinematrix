import Cookies from "js-cookie";

export default  function Logout() {
    function handleLogout() {
        Cookies.remove("jwtToken");
        window.location.href = '/';
    }
    return (
        <button
            onClick={handleLogout}
            className="bg-red-500 hover:bg-red-600 text-white font-bold py-2 px-4 rounded-lg focus:outline-none focus:shadow-outline">
            Logout
        </button>
    );
}