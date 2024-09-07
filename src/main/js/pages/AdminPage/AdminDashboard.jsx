const categories = [
    { route: 'accounts', name: 'Tài khoản', component: null },
    { route: 'accounts', name: 'Nội dung', component: null },
    { route: 'accounts', name: 'Thống kê', component: null }
]

function Card({ cardData }) {
    const {route, name, component} = cardData;

    return (
        // <Link>
            <div className="border border-white h-full min-h-[200px] hover:shadow-2xl text-center">
                <p className="text-3xl font-bold align-middle">{name}</p>
            </div>
        // {/* </Link> */}
    )
}

export function AdminDashboard() {
    return (
        <>
            <div className="grid grid-cols-2 gap-4">
                {categories.map((item, index) => (
                    <Card key={index} cardData={item} />
                ))}
            </div>
        </>
    )
}