import './App.css';
///import { lazy } from 'react';
import { Route, Routes } from 'react-router-dom';

import Layout from "./layouts/Layout";
import SimpleLayout from "./layouts/SimpleLayout";
import NotFound from "./pages/NotFound";
import Upload from "./pages/Upload";
import UserDetail from "./pages/UserDetail";
import ContentDetail from "./pages/ContentDetail";
import  AdminPage from "./pages/AdminPage";
import { AllVerifiedMovie } from "./pages/AllVerifiedContent/AllVerifiedMovie";
import ResetPassword from './pages/ResetPassword';
import { VerifyOtp } from './pages/ResetPassword/VerifyOtp';
import { AdminDashboard } from './pages/AdminPage/AdminDashboard';
import { BanUser } from './pages/AdminPage/BanUser';
import { AllVerifiedTv } from "./pages/AllVerifiedContent/AllVerifiedTv";
import SearchContent from "./pages/SearchContent";
import {lazy, Suspense} from "react";

import Home from "./pages/Home";
// import Login from "./pages/Login";
// import Register from "./pages/Register";
import Watch from "./pages/Watch";
import Sus from "./utils/Sus.tsx";

// const Home = lazy(() => namedImport("Home", "./pages/Home"));
// const Home = lazy(() => import("./pages/Home"));
const Login = Sus(lazy(() => import("./pages/Login")));
const Register = Sus(lazy(() => import("./pages/Register")));
// const Watch = lazy(() => import("./pages/Watch"));

export default function App() {
    return (
        <Routes>
            <Route path="/" element={<Layout/>}>
                <Route index element={<Home/>}/>
                <Route path={'/user/detail'} element={<UserDetail/>}/>
                <Route path={'/admin'} element={<AdminPage/>}/>
                <Route path={'/admin/dashboard'} element={<AdminDashboard/>}/>
                <Route path={'/admin/ban-user'} element={<BanUser/>}/>
                <Route path={'/content/search'} element={<SearchContent/>}/>
                <Route path={'/content/movie/all'} element={<AllVerifiedMovie/>}/>
                <Route path={'/content/tv/all'} element={<AllVerifiedTv/>}/>
                <Route path={'/content/detail/:id'} element={<ContentDetail/>}/>
                <Route path='/upload' element={<Upload/>}/>
                <Route path='/watch/:id' element={<Watch/>}/>
            </Route>
            <Route element={<SimpleLayout/>}>
                <Route path='/login' element={<Login/>}/>
                <Route path='/register' element={<Register/>}/>
                <Route path='/reset-password' element={<ResetPassword/>}/>
                <Route path='/verify-otp' element={<VerifyOtp/>}/>
                <Route path='*' element={<NotFound/>}/>
            </Route>
        </Routes>
    );
}

if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register("../firebase-messaging-sw.js", {scope: '/'})
        .then(function (registration) {
            console.log('Registration firebase message sw successful, scope is:', registration.scope);
        })
        .catch(function (err) {
            console.log("firebase message sw registration failed, err: ", err);
        });
}


