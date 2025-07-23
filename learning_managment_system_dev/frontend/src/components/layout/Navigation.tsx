'use client'

import React, { useState } from 'react'
import Link from 'next/link'
import { usePathname } from 'next/navigation'
import { LogOut as LogoutIcon } from 'lucide-react';
import {
  HomeIcon,
  BookOpenIcon,
  GroupIcon,  // <-- ici
  CogIcon,
  LogOutIcon,
  MenuIcon,
  XIcon,
  UserIcon,
  GraduationCapIcon,
} from 'lucide-react'


import { Button } from '@/components/ui/Button'
import { authService } from '@/services/auth'
import { cn } from '@/lib/utils'

interface NavigationItem {
  name: string
  href: string
  icon: any
  roles: string[]
}

const navigationItems: NavigationItem[] = [
  {
    name: 'Dashboard',
    href: '/dashboard',
    icon: HomeIcon,
    roles: ['admin', 'teacher', 'student']
  },
  {
    name: 'Courses',
    href: '/courses',
    icon: BookOpenIcon,
    roles: ['admin', 'teacher']
  },
  {
    name: 'Students',
    href: '/students',
    icon: GroupIcon,
    roles: ['admin', 'teacher']
  },
  {
    name: 'Admins',
    href: '/admin',
    icon: GroupIcon,
    roles: ['admin']
  },
  {
    name: 'Teachers',
    href: '/teachers',
    icon: GraduationCapIcon,
    roles: ['admin', 'teacher']
  },
  {
    name: 'Profile',
    href: '/profile',
    icon: UserIcon,
    roles: ['admin', 'teacher', 'student']
  },
  {
    name: 'Settings',
    href: '/settings',
    icon: CogIcon,
    roles: ['admin', 'teacher', 'student']
  }
]

export default function Navigation() {
  const [isOpen, setIsOpen] = useState(false)
  const pathname = usePathname()
  const user = authService.getUserMail()
  const userRole = authService.getUserRole()

  const filteredItems = navigationItems.filter(item =>
    item.roles.includes(userRole)
  )

  const handleLogout = async () => {
    await authService.logout()
    window.location.href = '/login'
  }

  return (
    <nav className="bg-white shadow-sm border-b">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          <div className="flex">
            <div className="flex-shrink-0 flex items-center">
              <Link href="/dashboard" className="text-xl font-bold text-blue-600">
                LMS
              </Link>
            </div>
            <div className="hidden md:ml-6 md:flex md:space-x-8">
              {filteredItems.map((item) => {
                const Icon = item.icon
                return (
                  <Link
                    key={item.name}
                    href={item.href}
                    className={cn(
                      'inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium',
                      pathname === item.href
                        ? 'border-blue-500 text-blue-600'
                        : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                    )}
                  >
                    <Icon className="h-4 w-4 mr-2" />
                    {item.name}
                  </Link>
                )
              })}
            </div>
          </div>
          <div className="hidden md:ml-6 md:flex md:items-center md:space-x-4">
            <div className="text-sm text-gray-700">
              Welcome
            </div>
            <Button
              onClick={handleLogout}
              variant="outline"
              size="sm"
              className="inline-flex items-center"
            >
              <LogoutIcon className="h-4 w-4 mr-2" />
              Logout
            </Button>
          </div>
          <div className="md:hidden flex items-center">
            <Button
              onClick={() => setIsOpen(!isOpen)}
              variant="ghost"
              size="sm"
            >
              {isOpen ? (
                <XIcon className="h-6 w-6" />
              ) : (
                <MenuIcon className="h-6 w-6" />
              )}
            </Button>
          </div>
        </div>
      </div>

      {isOpen && (
        <div className="md:hidden">
          <div className="pt-2 pb-3 space-y-1 sm:px-3">
            {filteredItems.map((item) => {
              const Icon = item.icon
              return (
                <Link
                  key={item.name}
                  href={item.href}
                  className={cn(
                    'block px-3 py-2 rounded-md text-base font-medium',
                    pathname === item.href
                      ? 'bg-blue-50 text-blue-700'
                      : 'text-gray-600 hover:text-gray-900 hover:bg-gray-50'
                  )}
                  onClick={() => setIsOpen(false)}
                >
                  <Icon className="h-4 w-4 mr-2 inline" />
                  {item.name}
                </Link>
              )
            })}
            <div className="border-t border-gray-200 pt-4">
              <div className="px-3 py-2 text-sm text-gray-700">
                Welcome,
              </div>
              <Button
                onClick={handleLogout}
                variant="outline"
                size="sm"
                className="mx-3 mt-2"
              >
                <LogoutIcon className="h-4 w-4 mr-2" />
                Logout
              </Button>
            </div>
          </div>
        </div>
      )}
    </nav>
  )
}