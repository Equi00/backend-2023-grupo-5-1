package ar.edu.unsam.phm.grupo5.airphm

import ar.edu.unsam.phm.grupo5.airphm.dto.ReserveDTO
import ar.edu.unsam.phm.grupo5.airphm.graph.*
import ar.edu.unsam.phm.grupo5.airphm.lodgment.*
import ar.edu.unsam.phm.grupo5.airphm.repositories.LodgmentRepository
import ar.edu.unsam.phm.grupo5.airphm.repositories.RateRepository
import ar.edu.unsam.phm.grupo5.airphm.repositories.ReserveRepository
import ar.edu.unsam.phm.grupo5.airphm.repositories.UserRepository
import ar.edu.unsam.phm.grupo5.airphm.reserve.Reserve
import ar.edu.unsam.phm.grupo5.airphm.user.User
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*
import javax.persistence.EntityManager

@Service
class AirphmBootstrap : InitializingBean {

    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var lodgmentRepository: LodgmentRepository
    @Autowired
    private lateinit var lodgementGraphRepository: LodgementGraphRepository
    @Autowired
    private lateinit var rateDataRepository: RateRepository
    @Autowired
    private lateinit var reserveRepository: ReserveRepository
    @Autowired
    private lateinit var reserveGraphRepository: ReserveGraphRepository
    @Autowired
    private lateinit var userGraphRepository: UserGraphRepository

    private lateinit var user1: User
    private lateinit var user2: User
    private lateinit var user3: User
    private lateinit var user4: User

    private lateinit var cabin1: Cabin
    private lateinit var cabin2: Cabin
    private lateinit var cabin3: Cabin
    private lateinit var cabin4: Cabin

    private lateinit var house1: House
    private lateinit var house2: House
    private lateinit var house3: House
    private lateinit var house4: House

    private lateinit var department1: Department
    private lateinit var department2: Department
    private lateinit var department3: Department
    private lateinit var department4: Department

    private lateinit var cabin11: Lodgment
    private lateinit var cabin22: Lodgment
    private lateinit var cabin33: Lodgment
    private lateinit var cabin44: Lodgment

    private lateinit var house11: Lodgment
    private lateinit var house22: Lodgment
    private lateinit var house33: Lodgment
    private lateinit var house44: Lodgment

    private lateinit var department11: Lodgment
    private lateinit var department22: Lodgment
    private lateinit var department33: Lodgment
    private lateinit var department44: Lodgment

    private lateinit var reserve1: Reserve
    private lateinit var reserve2: Reserve
    private lateinit var reserve3: Reserve
    private lateinit var reserve4: Reserve
    private lateinit var reserve5: Reserve


    fun createUser(user : User): User {
        val userEntity = userRepository.findById(user.id).orElse(userRepository.save(user))

        createUserGraph(userEntity)

        return userEntity
    }

    fun createUserGraph(user: User) {
        userGraphRepository.findById(user.id).orElse(userGraphRepository.save(UserGraph(user.id)))
    }


    fun createRate(rate: RateData): RateData =
        rateDataRepository.findById(rate.id).orElse(rateDataRepository.save(rate))

    fun createReserve(reserve: Reserve): Reserve {

        val reserveEntity = reserveRepository.findById(reserve.id).orElse(reserveRepository.save(reserve))

        if (reserveEntity != null) {
            val lodgementEntity = lodgmentRepository.findById(reserve.lodgmentId).get()
            var lodgementGraph = lodgementGraphRepository.findByLodgementId(reserve.lodgmentId)

            var userGraph = userGraphRepository.findById(reserve.user.id)

            if(lodgementGraph.isEmpty) {
                lodgementGraph = Optional.of(lodgementGraphRepository.save(LodgementGraph().fromLodgement(lodgementEntity, lodgementEntity.javaClass.simpleName)))
            }

            if (userGraph.isEmpty) {
                userGraph = Optional.of(userGraphRepository.save(UserGraph(reserve.user.id)))
            }

            userGraphRepository.createReservedByRelationship(userGraph.get().id!!, lodgementGraph.get().lodgementId!!, reserveEntity.startDate)
        }

        return reserveEntity
    }



//    fun createLodgment(lodgment: Lodgment): Lodgment =
//        lodgmentRepository.findById(lodgment.id).orElse(lodgmentRepository.save(lodgment))



    fun initUsers() {
        user1 = User("Ezequiel", "Oyola", "Argentina", 800000, LocalDate.of(2000,6, 14))
            .apply {
                email = "ezeloyola3@gmail.com"
                this.password = "Equi00"
                reserves = mutableSetOf()
                friends = mutableListOf()
            }
        user2 = User(
            "Mateo",
            "Pastorini",
            "Argentina",
            800000,
            LocalDate.of(1998,6, 9))
            .apply {
                email = "matepasto@gmail.com"
                this.password = "mate123"
                reserves = mutableSetOf()
                friends = mutableListOf()
            }
        user3 = User(
            "Julian",
            "Zurlo",
            "Argentina",
            800000,
            LocalDate.of(1990,6, 1))
            .apply {
                email = "julizurlo@gmail.com"
                this.password = "zurlo321"
                reserves = mutableSetOf()
                friends = mutableListOf()
            }
        user4 = User(
            "Lucas",
            "Klassen",
            "Argentina",
            800000,
            LocalDate.of(1998,8, 20))
            .apply {
                email = "lucasklassen@gmail.com"
                this.password = "lucas456"
                reserves = mutableSetOf()
                friends = mutableListOf()

            }
        user1 = this.createUser(user1)
        user2 = this.createUser(user2)
        user3 = this.createUser(user3)
        user4 = this.createUser(user4)
    }

    fun initLodgments() {
        val desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi porta, neque eget ullamcorper pellentesque, mi nunc varius lorem, vel molestie nulla mauris nec turpis. Nunc sollicitudin tortor in massa varius laoreet. Aliquam rhoncus enim libero, nec ultrices neque pharetra ornare. Aenean et lacus vel dui sollicitudin eleifend rhoncus nec dolor."
        val link = "https://www.losandes.com.ar/resizer/p--t4lbcubyFqgvm51EySkpaPgk=/980x640/smart/filters:quality(75):format(webp)/cloudfront-us-east-1.images.arcpublishing.com/grupoclarin/HA2GKZJWGY3TMOJQGE2DANJRHA.jpg"

        cabin1 = Cabin(user2.id, 5000, "Cabania 1", desc, 4, 3, 1, "Algo", "Tiene wifi", false, "Calle 123", "Chile", link).apply {
            //rate = mutableListOf(RateData(4, "MUY BUENA", user1), RateData(5, "LA MEJOR", user4), RateData(2, "POCO ESPACIO", user3), RateData(2, "POCA LUZ NATURAL", user3))
            //reserves = mutableListOf(Reserve(user1, this.id, LocalDate.now(), LocalDate.now().plusDays(6), this.totalCost()))
        }
        cabin2 = Cabin(user2.id, 5000, "Cabania 2", desc, 4, 3, 1, "Algo", "Tiene wifi", false, "Calle 123", "Argentina", link)
        cabin3 = Cabin(user2.id, 5000, "Cabania 3", desc, 4, 3, 1, "Algo", "Tiene wifi", false, "Calle 123", "Argentina", link)
        cabin4 = Cabin(user2.id, 5000, "Cabania 4", desc, 4, 3, 1, "Algo", "Tiene wifi", false, "Calle 123", "Canada", link)

        house1 = House(user3.id, 5000, "Casa 1", desc, 4, 3, 1, "Algo", "Tienewifi", false, "Calle 123", "Uruguay", link)
        house2 = House(user3.id, 5000, "Casa 2", desc, 4, 3, 1, "Algo", "Tienewifi", false, "Calle 123", "España", link)
        house3 = House(user3.id, 5000, "Casa 3", desc, 4, 3, 1, "Algo", "Tienewifi", false, "Calle 123", "Estados Unidos", link)
        house4 = House(user3.id, 5000, "Casa 4", desc, 4, 3, 1, "Algo", "Tienewifi", false, "Calle 123", "Chile", link)

        department1 = Department(user4.id, 5000, "Departamento 1", desc, 4, 3, 1, "Algo", "Tienewifi", false, "Calle 123", "Argentina", link)
        department2 = Department(user4.id, 5000, "Departamento 2", desc, 4, 3, 1, "Algo", "Tienewifi", false, "Calle 123", "Canada", link)
        department3 = Department(user4.id, 5000, "Departamento 3", desc, 4, 3, 1, "Algo", "Tienewifi", false, "Calle 123", "Japon", link)
        department4 = Department(user4.id, 5000, "Departamento 4", desc, 4, 3, 1, "Algo", "Tienewifi", false, "Calle 123", "Estados Unidos", link)

//        this.createLodgment(cabin1)
//        this.createLodgment(cabin2)
//        this.createLodgment(cabin3)
//        this.createLodgment(cabin4)
//        this.createLodgment(house1)
//        this.createLodgment(house2)
//        this.createLodgment(house3)
//        this.createLodgment(house4)
//        this.createLodgment(department1)
//        this.createLodgment(department2)
//        this.createLodgment(department3)
//        this.createLodgment(department4)

        cabin11 = lodgmentRepository.createIfNotExists(cabin1)
        cabin22 = lodgmentRepository.createIfNotExists(cabin2)
        cabin33 = lodgmentRepository.createIfNotExists(cabin3)
        cabin44 = lodgmentRepository.createIfNotExists(cabin4)
        house11 = lodgmentRepository.createIfNotExists(house1)
        house22 = lodgmentRepository.createIfNotExists(house2)
        house33 = lodgmentRepository.createIfNotExists(house3)
        house44 = lodgmentRepository.createIfNotExists(house4)
        department11 = lodgmentRepository.createIfNotExists(department1)
        department22 = lodgmentRepository.createIfNotExists(department2)
        department33 = lodgmentRepository.createIfNotExists(department3)
        department44 = lodgmentRepository.createIfNotExists(department4)
    }

    fun initRate(){
        this.createRate(RateData(4, "MUY BUENA", user1, cabin11.id))
        this.createRate(RateData(5, "LA MEJOR", user4, cabin11.id))
        this.createRate(RateData(2, "POCO ESPACIO", user3, cabin11.id))
        this.createRate(RateData(2, "POCA LUZ NATURAL", user3, cabin11.id))

        cabin11.rateCount = rateDataRepository.findAll().count()
        cabin11.rateAverage = rateDataRepository.findAll().sumOf { it.rateScore }.toFloat() / rateDataRepository.findAll().count()

        lodgmentRepository.save(cabin11)
    }

    fun initUserData() {
        user1.apply{
            addFriend(user2)
            addFriend(user3)
            lodgmentReserve(house33.id, LocalDate.now(), LocalDate.of(2023, 7, 15), house33.totalCost())
            lodgmentReserve(department22.id, LocalDate.now(), LocalDate.of(2023, 7, 15), department22.totalCost())
        }
        user2.apply{
            addFriend(user1)
            addFriend(user4)
        }
        user3.apply{
            addFriend(user1)
        }
        user4.apply{
            addFriend(user2)
            lodgmentReserve(house11.id, LocalDate.now(), LocalDate.of(2023, 7, 15), house11.totalCost())
        }

        userRepository.saveAll(
            setOf(
                user1,
                user2,
                user3,
                user4
            )
        )

        cabin11.apply {
            reserves = mutableListOf(ReserveDTO(LocalDate.now(), LocalDate.now().plusDays(6)))
        }

        lodgmentRepository.updateLodgment(cabin11)


        //Init graph data
        val user1Graph = userGraphRepository.findById(user1.id).get()
        val user2Graph = userGraphRepository.findById(user2.id).get()
        val user3Graph = userGraphRepository.findById(user3.id).get()
        val user4Graph = userGraphRepository.findById(user4.id).get()

        user1Graph.apply {
            friends = mutableListOf(user2Graph, user3Graph)
        }

        user2Graph.apply {
            friends = mutableListOf(user1Graph, user4Graph)
        }

        user3Graph.apply {
            friends = mutableListOf(user1Graph)
        }

        user4Graph.apply {
            friends = mutableListOf(user2Graph)
        }

        userGraphRepository.saveAll(
            setOf(
                user1Graph,
                user2Graph,
                user3Graph,
                user4Graph
            )
        )
    }

    fun initReserve(){
        reserve1 = Reserve(user1, cabin11.id, LocalDate.now(), LocalDate.now().plusDays(6), cabin11.totalCost())
        reserve2 = Reserve(user3, house11.id, LocalDate.now(), LocalDate.now().plusDays(6), house11.totalCost())
        reserve3 = Reserve(user2, cabin22.id, LocalDate.now(), LocalDate.now().plusDays(6), cabin22.totalCost())
        reserve4 = Reserve(user4, department11.id, LocalDate.now(), LocalDate.now().plusDays(6), department11.totalCost())
        reserve5 = Reserve(user1, cabin33.id, LocalDate.now(), LocalDate.now().plusDays(6), cabin33.totalCost())

        this.createReserve(reserve1)
        this.createReserve(reserve2)
        this.createReserve(reserve3)
        this.createReserve(reserve4)
        this.createReserve(reserve5)

        cabin11.apply {
            reserves = mutableListOf(ReserveDTO(reserve1.startDate, reserve1.endDate))
        }
        house11.apply { reserves = mutableListOf(ReserveDTO(reserve2.startDate, reserve2.endDate)) }
        cabin22.apply { reserves = mutableListOf(ReserveDTO(reserve3.startDate, reserve3.endDate)) }
        department11.apply { reserves = mutableListOf(ReserveDTO(reserve4.startDate, reserve4.endDate)) }
        cabin33.apply { reserves = mutableListOf(ReserveDTO(reserve5.startDate, reserve5.endDate)) }

        lodgmentRepository.updateLodgment(cabin11)
        lodgmentRepository.updateLodgment(house11)
        lodgmentRepository.updateLodgment(cabin22)
        lodgmentRepository.updateLodgment(department11)
        lodgmentRepository.updateLodgment(cabin33)
    }

    fun LodgmentRepository.createIfNotExists(lodgment: Lodgment): Lodgment {
        val bdLodgment = this.findByName(lodgment.name)
        return if (bdLodgment === null) {
            this.save(lodgment)
            lodgment
        } else {
            bdLodgment
        }
    }

    fun LodgmentRepository.updateLodgment(lodgment: Lodgment): Lodgment{
        val bdLodgment = this.findById(lodgment.id).get()
        bdLodgment.reserves = lodgment.reserves
        return this.save(bdLodgment)
    }

    override fun afterPropertiesSet() {
        initUsers()
        initLodgments()
        initRate()
        initUserData()
        initReserve()
    }
}
