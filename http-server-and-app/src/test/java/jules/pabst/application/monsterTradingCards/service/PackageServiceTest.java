package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.DTOs.AquirePackageDTO;
import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.AllPackagesOwned;
import jules.pabst.application.monsterTradingCards.exception.NotAuthorized;
import jules.pabst.application.monsterTradingCards.exception.NotEnoughCredit;
import jules.pabst.application.monsterTradingCards.repository.PackageRepository;
import jules.pabst.application.monsterTradingCards.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PackageServiceTest {

    private PackageRepository packageRepository;
    private UserRepository userRepository;
    private CardService cardService;
    private UserService userService;
    private PackageService packageService;

    @BeforeEach
    void setUp() {
        packageRepository = mock(PackageRepository.class);
        userRepository = mock(UserRepository.class);
        cardService = mock(CardService.class);
        userService = mock(UserService.class);

        packageService = new PackageService(packageRepository, userRepository, cardService, userService);
    }

    @Test
    void givenAdminToken_whenCreatePackage_thenSuccess() {
        // Arrange
        String adminToken = "admin-mtcgToken";
        List<Card> cards = Arrays.asList(new Card(), new Card(), new Card(), new Card());

        // Act
        List<Card> createdCards = packageService.createPackage(adminToken, cards);

        // Assert
        verify(packageRepository, times(1)).save(anyString());
        verify(cardService, times(4)).create(any(Card.class));
        assertEquals(4, createdCards.size());
    }

    @Test
    void givenNonAdminToken_whenCreatePackage_thenThrowsNotAuthorized() {
        // Arrange
        String nonAdminToken = "user-mtcgToken";
        List<Card> cards = Arrays.asList(new Card(), new Card(), new Card());

        // Act & Assert
        assertThrows(NotAuthorized.class, () -> packageService.createPackage(nonAdminToken, cards));
        verify(packageRepository, never()).save(anyString());
        verify(cardService, never()).create(any(Card.class));
    }

    @Test
    void givenUserWithEnoughCredit_whenCheckCreditAndAquire_thenSuccess() {
        // Arrange
        String authToken = "user-mtcgToken";
        User user = new User();
        user.setUuid(UUID.randomUUID().toString());
        user.setCredit(10);

        when(userService.getUserByAuthenticationToken(authToken)).thenReturn(user);
        when(packageRepository.findPackageWithoutOwner()).thenReturn("package-id");
        when(packageRepository.updatePackage(anyString(), any(User.class)))
                .thenReturn(new AquirePackageDTO("package-id", user.getUuid()));

        // Act
        AquirePackageDTO result = packageService.checkCreditAndAquire(authToken);

        // Assert
        verify(userRepository, times(1)).updateUserByUuid(user);
        verify(packageRepository, times(1)).updatePackage("package-id", user);
        assertEquals("package-id", result.getPackageId());
    }

    @Test
    void givenUserWithNotEnoughCredit_whenCheckCreditAndAquire_thenThrowsNotEnoughCredit() {
        // Arrange
        String authToken = "user-mtcgToken";
        User user = new User();
        user.setCredit(2);

        when(userService.getUserByAuthenticationToken(authToken)).thenReturn(user);

        // Act & Assert
        assertThrows(NotEnoughCredit.class, () -> packageService.checkCreditAndAquire(authToken));
        verify(packageRepository, never()).updatePackage(anyString(), any(User.class));
        verify(userRepository, never()).updateUserByUuid(user);
    }

    @Test
    void givenNoPackagesAvailable_whenCheckCreditAndAquire_thenThrowsAllPackagesOwned() {
        // Arrange
        String authToken = "user-mtcgToken";
        User user = new User();
        user.setCredit(10);

        when(userService.getUserByAuthenticationToken(authToken)).thenReturn(user);
        when(packageRepository.findPackageWithoutOwner()).thenThrow(new AllPackagesOwned("All packages are already owned"));

        // Act & Assert
        assertThrows(AllPackagesOwned.class, () -> packageService.checkCreditAndAquire(authToken));
        verify(userRepository, never()).updateUserByUuid(user);
    }
}
