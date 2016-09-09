package com.jordylangen.woodstorage.view

import android.content.Context
import com.jordylangen.woodstorage.*
import rx.Observable
import rx.Subscription

class WoodStoragePresenterSpec extends RxSpecification {

    Storage storage
    WoodStorageContract.View view
    WoodStoragePresenter presenter

    def setup() {
        storage = Mock(Storage)
        view = Mock(WoodStorageContract.View)
        presenter = new WoodStoragePresenter()

        WoodStorageFactory.getInstance(null, new StorageFactory() {
            @Override
            Storage create(Context context) {
                return storage
            }
        })
    }

    def "should subscribe on setup"() {
        when:
        presenter.setup(view)

        then:
        1 * storage.load() >> Observable.empty()
        0 * view._
    }

    def "should add all logs to the view in normal order"() {
        given:
        def count = 10;
        def logs = []

        for (def index = 0; index < count; index++) {
            logs.add(new LogEntry("spec", 0, Integer.toString(index)))
        }

        def observable = Observable.from(logs)
        storage.load() >> observable

        when:
        presenter.setup(view)

        then:
        count * view.add(_ as LogEntry)
    }

    def "should clear all logs upon sort order inversion and add them again inverted"() {
        given:
        def count = 10;
        def logs = []

        for (def index = 0; index < count; index++) {
            logs.add(new LogEntry("spec", 0, Integer.toString(index)))
        }

        def observable = Observable.from(logs)
        storage.load() >> observable

        when:
        presenter.setup(view)

        then:
        count * view.add(_ as LogEntry)

        when:
        presenter.onOptionsItemSelected(R.id.woodstorage_action_sort)

        then:
        1 * view.clear()
        count * view.addAt(_ as LogEntry, 0)
    }

    def "should clear logs"() {
        when:
        presenter.setup(view)

        then:
        1 * storage.load() >> Observable.empty()

        when:
        presenter.onOptionsItemSelected(R.id.woodstorage_action_clear)

        then:
        1 * storage.load() >> Observable.empty()
        1 * storage.clear()
        1 * view.clear()
    }

    def "should unsubscribe all subscriptions upon teardown"() {
        given:
        def logEntriesSubscription = Mock(Subscription)
        def selectedTagsSubscription = Mock(Subscription)
        presenter.logEntriesSubscription = logEntriesSubscription
        presenter.selectedTagsSubscription = selectedTagsSubscription

        when:
        presenter.teardown()

        then:
        1 * logEntriesSubscription.isUnsubscribed()
        1 * logEntriesSubscription.unsubscribe()
        1 * selectedTagsSubscription.isUnsubscribed()
        1 * selectedTagsSubscription.unsubscribe()
    }

    def "should show the tag filter dialog and filter the logs when filters are applied"() {
        given:
        def selectableTags = [
                new SelectableTag("MyActivity", false),
                new SelectableTag("MyService", true),
                new SelectableTag("MyFragment", false)
        ]

        def tagFilterPresenter = Mock(TagFilterContract.Presenter)
        tagFilterPresenter.observeSelectedTags() >> Observable.from(selectableTags).toList()

        def logs = [
                new LogEntry("MyActivity", 1, "onCreate of Activity"),
                new LogEntry("MyService", 1, "onCreate of MyService"),
                new LogEntry("MyFragment", 1, "onCreate of MyFragment")
        ]

        storage.load() >> Observable.from(logs)

        PresenterCache.put(R.id.dialog_tag_filter, tagFilterPresenter)

        when:
        presenter.setup(view)

        then:
        3 * view.add(_ as LogEntry)

        when:
        presenter.onOptionsItemSelected(R.id.woodstorage_action_filter)

        then:
        1 * view.showTagFilterDialog()
        1 * view.clear()
        1 * view.add(_ as LogEntry) >> { LogEntry logEntry ->
            assert logEntry.tag == "MyService"
        }
    }
}