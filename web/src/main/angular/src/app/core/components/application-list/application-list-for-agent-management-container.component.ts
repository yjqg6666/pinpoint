import { Component, OnInit, OnDestroy, ComponentFactoryResolver, Injector } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Subject, forkJoin, EMPTY } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';

import {
    WebAppSettingDataService,
    StoreHelperService,
    AnalyticsService,
    TRACKED_EVENT_LIST,
    MessageQueueService,
    MESSAGE_TO,
    ApplicationListDataService,
    DynamicPopupService
} from 'app/shared/services';
import { ApplicationListInteractionForConfigurationService } from './application-list-interaction-for-configuration.service';
import { FOCUS_TYPE } from './application-list-for-header.component';
import { isEmpty } from 'app/core/utils/util';
import { ServerErrorPopupContainerComponent } from 'app/core/components/server-error-popup/server-error-popup-container.component';

@Component({
    selector: 'pp-application-list-for-agent-management-container',
    templateUrl: './application-list-for-agent-management-container.component.html',
    styleUrls: ['./application-list-for-agent-management-container.component.css']
})
export class ApplicationListForAgentManagementContainerComponent implements OnInit, OnDestroy {
    private unsubscribe = new Subject<void>();
    private _query = '';
    private originalAppList: IApplication[];

    filteredAppList: IApplication[];
    funcImagePath: Function;
    selectedApp: IApplication;
    showTitle = false;
    focusType: FOCUS_TYPE = FOCUS_TYPE.KEYBOARD;
    restCount = 0;
    focusIndex = -1;
    searchUseEnter = false;
    SEARCH_MIN_LENGTH = 2;
    i18nText = {
        SEARCH_INPUT_GUIDE: '',
        EMPTY: ''
    };
    isEmpty: boolean;

    constructor(
        private storeHelperService: StoreHelperService,
        private webAppSettingDataService: WebAppSettingDataService,
        private translateService: TranslateService,
        private messageQueueService: MessageQueueService,
        private applicationListDataService: ApplicationListDataService,
        private applicationListInteractionForConfigurationService: ApplicationListInteractionForConfigurationService,
        private analyticsService: AnalyticsService,
        private dynamicPopupService: DynamicPopupService,
        private componentFactoryResolver: ComponentFactoryResolver,
        private injector: Injector
    ) {}

    ngOnInit() {
        this.initList();
        this.initI18nText();
        this.funcImagePath = this.webAppSettingDataService.getIconPathMakeFunc();
        this.messageQueueService.receiveMessage(this.unsubscribe, MESSAGE_TO.APPLICATION_REMOVED).pipe(
            switchMap(() => {
                return this.applicationListDataService.getApplicationList().pipe(
                    catchError((error: IServerErrorFormat) => {
                        this.dynamicPopupService.openPopup({
                            data: {
                                title: 'Server Error',
                                contents: error
                            },
                            component: ServerErrorPopupContainerComponent,
                        }, {
                            resolver: this.componentFactoryResolver,
                            injector: this.injector
                        });

                        return EMPTY;
                    })
                );
            })
        ).subscribe(() => {
            this.selectedApp = null;
        });
    }

    ngOnDestroy() {
        this.unsubscribe.next();
        this.unsubscribe.complete();
        this.applicationListInteractionForConfigurationService.setSelectedApplication(null);
    }

    private initList(): void {
        this.storeHelperService.getApplicationList(this.unsubscribe).subscribe((appList: IApplication[]) => {
            this.originalAppList = appList;
            this.filterList();
        });
    }

    private initI18nText(): void {
        forkJoin(
            this.translateService.get('COMMON.INPUT_APP_NAME_PLACE_HOLDER'),
            this.translateService.get('COMMON.EMPTY_ON_SEARCH')
        ).subscribe(([placeholderText, emptyText]: string[]) => {
            this.i18nText.SEARCH_INPUT_GUIDE = placeholderText;
            this.i18nText.EMPTY = emptyText;
        });
    }

    private selectApp(app: IApplication): void {
        if (!app) {
            return;
        }

        this.selectedApp = app;
    }

    private filterList(): void {
        if (this.query === '') {
            this.filteredAppList = this.originalAppList;
        } else {
            this.filteredAppList = this.originalAppList.filter((application: IApplication) => {
                return new RegExp(this.query, 'i').test(application.getApplicationName());
            });
        }

        this.isEmpty = isEmpty(this.filteredAppList);
    }

    private set query(query: string) {
        this._query = query;
        this.filterList();
    }

    private get query(): string {
        return this._query;
    }

    onSearch(query: string): void {
        if (this.query === query) {
            return;
        }

        this.query = query;
    }

    onCancel(): void {
        this.query = '';
    }


    onSelectApp(app: IApplication): void {
        if (app.equals(this.selectedApp)) {
            return;
        }

        this.selectApp(app);
        this.applicationListInteractionForConfigurationService.setSelectedApplication(app);
        this.analyticsService.trackEvent(TRACKED_EVENT_LIST.SELECT_APPLICATION_FOR_ALARM);
    }

    onFocused(index: number): void {
        this.focusIndex = index;
        this.focusType = FOCUS_TYPE.MOUSE;
    }
}
