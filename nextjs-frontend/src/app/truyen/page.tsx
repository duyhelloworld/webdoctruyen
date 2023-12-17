// File: pages/yourpage.tsx
'use client'
import { useEffect } from 'react';
import { useRouter } from 'next/navigation';

const YourPage: React.FC = () => {
    const router = useRouter();

    useEffect(() => {
        // Kiểm tra điều kiện nào đó (ví dụ: user chưa đăng nhập)
        const userNotLoggedIn = true; // Thay đổi điều kiện này theo yêu cầu của bạn

        // Nếu điều kiện không được đáp ứng, chuyển hướng về trang chủ
        if (userNotLoggedIn) {
            router.push('/');
        }
    }, []); // [] để chỉ chạy một lần khi component được render

    return (
        <div>
            {/* Nội dung trang */}
        </div>
    );
};

export default YourPage;
