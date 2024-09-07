export default function PosterImage({path}) {
    return (
        <>
            <img
                src={baseTmdbImgUrl + path}
                className={"rounded-2xl overflow-hidden inline-flex items-center justify-center bg-transparent text-gray-400"}
                alt={"Poster content"}
            />
        </>
    );
}
export const baseTmdbImgUrl = "https://image.tmdb.org/t/p/original";
