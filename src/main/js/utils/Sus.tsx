
import LoadingSpinner from "../components/LoadingSpinner/LoadingSpinner";
import { Suspense } from "react";

const Sus = (Component) => (props) => (
    <Suspense fallback={<LoadingSpinner/>}>
      <Component {...props} />
    </Suspense>
);

export default Sus;