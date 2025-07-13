package com.sgvas21.ddadi21.messengerapp.di

import com.sgvas21.ddadi21.messengerapp.data.repository.ChatRepository
import com.sgvas21.ddadi21.messengerapp.data.repository.ChatRepositoryImpl
import com.sgvas21.ddadi21.messengerapp.data.repository.UserRepository
import com.sgvas21.ddadi21.messengerapp.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindingUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindingChatRepository(
        chatRepositoryImpl: ChatRepositoryImpl
    ): ChatRepository
}